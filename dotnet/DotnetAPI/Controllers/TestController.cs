using DotnetAPI.Data;
using Microsoft.AspNetCore.Mvc;

namespace DotnetAPI.Controllers;

[ApiController]
[Route("[controller]")]
public class TestController : ControllerBase
{
    private readonly DataContextDapper _dapper;

    public TestController(IConfiguration config)
    {
        _dapper = new DataContextDapper(config);
    }

    /// <summary>
    /// Tests the database connection by retrieving the current date and time from the database server.
    /// </summary>
    /// <returns>The current date and time from the database server.</returns>
    [HttpGet("Connection")]
    public DateTime TestConnection()
    {
        // Execute a SQL command to retrieve the current server date and time
        return _dapper.LoadDataSingle<DateTime>("SELECT GETDATE()");
    }

    /// <summary>
    /// Basic endpoint to test if the application is running.
    /// </summary>
    /// <returns>A string message confirming that the application is operational.</returns>
    [HttpGet]
    public string Test()
    {
        // Return a simple message indicating that the application is running
        return "Your application is up and running.";
    }
}
