namespace UtahWildfires.Models;
using NetTopologySuite.Geometries;
using System.Text.Json.Serialization;



public class Wildfire
{
    public int Id { get; set; }

    public string? Name {get; set; }

    public string? FireCode {get; set; }

    public DateTime? Year {get; set;}

    public double? Acres {get; set;}

    public string? coordinates {get; set;}

    public Geometry? FireGeometry {get; set; }


}
