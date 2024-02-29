using System;
using System.Text.Json;
using System.Text.Json.Serialization;
using NetTopologySuite.Geometries;

namespace UtahWildfires.Helpers 
{
    public class GeometryJsonConverter : JsonConverter<Geometry>
    {
        public override Geometry Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
        {
            // Deserialization logic here (if necessary)
            throw new NotImplementedException();
        }

        public override void Write(Utf8JsonWriter writer, Geometry value, JsonSerializerOptions options)
        {
            var geometry = SimplifyGeometry(value); // Simplify geometry if needed

            if (geometry == null || geometry.IsEmpty)
            {
                writer.WriteNullValue();
            }
            else
            {
                writer.WriteStartObject();
                writer.WriteString("type", "Feature");

                writer.WritePropertyName("geometry");
                writer.WriteStartObject(); // Start geometry
                writer.WriteString("type", geometry.GeometryType);

                // Simplified coordinates writing, considering both Polygon and MultiPolygon
                writer.WritePropertyName("coordinates");
                if (geometry is Polygon polygon)
                {
                    WritePolygonCoordinates(writer, polygon);
                }
                else if (geometry is MultiPolygon multiPolygon)
                {
                    WriteMultiPolygonCoordinates(writer, multiPolygon);
                }
                // Additional geometry types can be added here
                writer.WriteEndObject(); // End geometry

                writer.WritePropertyName("properties");
                writer.WriteStartObject();
                // Example property
                writer.WriteEndObject(); // End properties

                writer.WriteEndObject(); // End the GeoJSON object
            }
        }

        private Geometry SimplifyGeometry(Geometry geometry)
        {
            // Use NTS's Simplify method with a tolerance parameter to simplify the geometry
            // Adjust the tolerance based on your needs
            return NetTopologySuite.Simplify.VWSimplifier.Simplify(geometry, .01);
        }

        private void WritePolygonCoordinates(Utf8JsonWriter writer, Polygon polygon)
        {
            writer.WriteStartArray(); // Start outer array
            foreach (var ring in new[] { polygon.Shell }.Concat(polygon.Holes))
            {
                writer.WriteStartArray(); // Start ring array
                foreach (var coord in ring.Coordinates)
                {
                    writer.WriteStartArray(); // Coordinate array
                    writer.WriteNumberValue(coord.X);
                    writer.WriteNumberValue(coord.Y);
                    writer.WriteEndArray(); // End coordinate array
                }
                writer.WriteEndArray(); // End ring array
            }
            writer.WriteEndArray(); // End outer array
        }

        private void WriteMultiPolygonCoordinates(Utf8JsonWriter writer, MultiPolygon multiPolygon)
        {
            writer.WriteStartArray(); // Start MultiPolygon array
            foreach (Polygon polygon in multiPolygon.Geometries)
            {
                WritePolygonCoordinates(writer, polygon); // Reuse the Polygon coordinates writing method
            }
            writer.WriteEndArray(); // End MultiPolygon array
        }
    }
}




