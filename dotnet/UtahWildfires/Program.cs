using UtahWildfires.Data;
using Microsoft.EntityFrameworkCore;
using Npgsql;
using NetTopologySuite.Geometries;
using UtahWildfires.Repositories;
using UtahWildfires.Helpers;


// Initialize the web application builder with the command-line arguments.
var builder = WebApplication.CreateBuilder(args);

// Retrieve the PostgreSQL connection string from the application's configuration, stored in appsettings.json.
var connectionString = builder.Configuration.GetConnectionString("DefaultConnection");

// Configure a data source builder specifically for Npgsql, passing the connection string.
var dataSourceBuilder = new NpgsqlDataSourceBuilder(connectionString);

// Enable NetTopologySuite on the data source builder, which adds support for spatial data types and operations.
dataSourceBuilder.UseNetTopologySuite();

// Build the data source object from the configured builder.
var dataSource = dataSourceBuilder.Build();

// Register the application's DbContext (DataContext) in the service container, configuring it to use Npgsql with our dataSource.
// The UseNetTopologySuite() option is specified to enable support for spatial data types within Entity Framework operations.
builder.Services.AddDbContext<DataContext>(options => 
    options.UseNpgsql(dataSource, o => o.UseNetTopologySuite()));

// Register the DataContext as a service in the application's dependency injection system.
// Scoped lifetime means an instance is created per request, ensuring that database operations are handled within a single transaction.
builder.Services.AddScoped<IDataContext>(provider => provider.GetService<DataContext>());

builder.Services.AddScoped<IWildfireRepository, WildfireRepository>();


// Add MVC services to the application, enabling the use of controllers and views for handling web requests and responses.
builder.Services.AddControllersWithViews();
    .AddJsonOptions(options =>
    {
        options.JsonSerializerOptions.Converters.Add(new UtahWildfires.Helpers.GeometryJsonConverter());
        // options.JsonSerializerOptions.NumberHandling = System.Text.Json.Serialization.JsonNumberHandling.AllowNamedFloatingPointLiterals;
        // options.JsonSerializerOptions.ReferenceHandler = System.Text.Json.Serialization.ReferenceHandler.Preserve;
    });


// Build the WebApplication instance with the configured services and settings.
var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    app.UseExceptionHandler("/Home/Error");
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}

app.UseHttpsRedirection();

app.UseStaticFiles();

app.UseRouting();

app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();
