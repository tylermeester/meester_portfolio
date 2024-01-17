using Microsoft.AspNetCore.Mvc;
using DotnetAPI.Data;
using DotnetAPI.Models;
using DotnetAPI.DTOs;


namespace DotnetAPI.Controllers;


[ApiController]
[Route("[controller]")]
public class UserController : ControllerBase
{

    /*------------------------------------------------------------------------------
    ------------------------- DAPPER DATABASE CONNECTION  --------------------------
    -------------------------------------------------------------------------------*/

    // Dapper context for database operations
    DataContextDapper _dapper;

    // Constructor initializes the Dapper context using the configuration provided
    public UserController(IConfiguration config)
    {
        // Retrieves and logs the database connection string for debugging purposes
        Console.WriteLine(config.GetConnectionString("DefaultConnection"));
        _dapper = new DataContextDapper(config);
    }


    /*------------------------------------------------------------------------------
    ------------------------------ GET ALL USERS -----------------------------------
    -------------------------------------------------------------------------------*/
    [HttpGet("GetUsers")]
    public IEnumerable<User> GetUsers()
    {
        string sql = @"
            SELECT [UserId],
                [FirstName],
                [LastName],
                [Email],
                [Gender],
                [Active] 
            FROM PortfolioProjectSchema.Users";

        IEnumerable<User> users = _dapper.LoadData<User>(sql);

        return users;
    }

    /*------------------------------------------------------------------------------
    ------------------------------ GET SINGLE USER ---------------------------------
    -------------------------------------------------------------------------------*/
    [HttpGet("GetSingleUser/{userId}")]
    public User GetSingleUser(int userId)
    {
        string sql = @"
            SELECT [UserId],
                [FirstName],
                [LastName],
                [Email],
                [Gender],
                [Active] 
            FROM PortfolioProjectSchema.Users
                WHERE UserId = " + userId.ToString();

        User user = _dapper.LoadDataSingle<User>(sql);

        return user;
    }

    /*------------------------------------------------------------------------------
    -------------------------------- UPDATE USER -------------------------------------
    -------------------------------------------------------------------------------*/
    [HttpPut("UpdateUser")]
    //Not returning data, IActionResult provides a response to indicate success/failure
    public IActionResult UpdateUser(User user)
    {
        string sql = @"
        UPDATE PortfolioProjectSchema.Users
            SET [FirstName] = '" + user.FirstName +
            "', [LastName] = '" + user.LastName +
            "', [Email] = '" + user.Email +
            "', [Gender] = '" + user.Gender +
            "', [Active] = '" + user.Active +
            "' WHERE UserId = " + user.UserId;

        Console.WriteLine(sql);

        if (_dapper.ExecuteSql(sql))
        {
            return Ok();
        }

        throw new Exception("Failed to update user");
    }


    /*------------------------------------------------------------------------------
    --------------------------------- ADD USER -------------------------------------
    -------------------------------------------------------------------------------*/
    [HttpPost("AddUser")]
    //Not returning data, IActionResult provides a response to indicate success/failure
    public IActionResult AddUser(AddUserDTO user)
    {

        string sql = @"INSERT INTO PortfolioProjectSchema.Users(
                [FirstName],
                [LastName],
                [Email],
                [Gender],
                [Active]
            )  VALUES (" +
                "'" + user.FirstName +
                "', '" + user.LastName +
                "', '" + user.Email +
                "', '" + user.Gender +
                "', '" + user.Active +
            "')";

        if (_dapper.ExecuteSql(sql))
        {
            return Ok();
        }

        throw new Exception("Failed to add user");
    }

    /*------------------------------------------------------------------------------
    ------------------------------- DELETE USER ------------------------------------
    -------------------------------------------------------------------------------*/
    //This is the route of the request
    [HttpDelete("DeleteUser/{userId}")]
    public IActionResult DeleteUser(int userId)
    {
        string sql = @"
                DELETE FROM PortfolioProjectSchema.Users
                    WHERE UserId = " + userId.ToString();

        if (_dapper.ExecuteSql(sql))
        {
            return Ok();
        }

        throw new Exception("Failed to delete user");
    }
}


