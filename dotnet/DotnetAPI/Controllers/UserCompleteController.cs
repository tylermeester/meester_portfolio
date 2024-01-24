using Microsoft.AspNetCore.Mvc;
using DotnetAPI.Data;
using DotnetAPI.Models;
using DotnetAPI.DTOs;
using Dapper;
using System.Data;
using DotnetAPI.Helpers;
using Microsoft.AspNetCore.Authorization;


namespace DotnetAPI.Controllers;

[Authorize]
[ApiController]
[Route("[controller]")]
public class UserCompleteController : ControllerBase
{
    /*------------------------------------------------------------------------------
    ----------------- DAPPER DATABASE CONNECTION AND CONSTRUCTOR -------------------
    -------------------------------------------------------------------------------*/
    private readonly DataContextDapper _dapper;
    private readonly ResuableSql _reusableSql;
    public UserCompleteController(IConfiguration config)
    {
        _dapper = new DataContextDapper(config);
        _reusableSql = new ResuableSql(config);
    }


    /*------------------------------------------------------------------------------
    --------------------------------- GET USERS ------------------------------------
    -------------------------------------------------------------------------------*/
    /// <summary>
    /// Retrieves a collection of UserComplete entities from the database. This method can 
    /// optionally filter users based on user ID and active status.
    /// </summary>
    /// <param name="userId">User ID to filter the users. Use 0 to ignore this filter.</param>
    /// <param name="isActive">Boolean to filter users based on active status.</param>
    /// <returns>An IEnumerable of UserComplete entities matching the criteria.</returns>
    [HttpGet("GetUsers/{userId}/{isActive}")]
    public IEnumerable<UserComplete> GetUsers(int userId, bool isActive)
    {
        // Start with default SQL procedure call for getting users
        string sql = @"EXEC PortfolioProjectSchema.spUsers_Get";
        string stringParameters = "";

        DynamicParameters sqlParameters = new DynamicParameters();

        // Add user ID to parameters if provided
        if (userId != 0)
        {
            stringParameters += ", @UserId=@UserIdParameter";
            sqlParameters.Add("@UserIdParameter", userId, DbType.Int32);
        }

        // Add active status to parameters if provided
        if (isActive)
        {
            stringParameters += ", @Active=@ActiveParameter";
            sqlParameters.Add("@ActiveParameter", isActive, DbType.Boolean);
        }

        // Append parameters to SQL query if any were added
        if (stringParameters.Length > 0)
        {
            sql += stringParameters.Substring(1);  // Remove leading comma
        }

        // Execute the query with any applied parameters and return the result
        return _dapper.LoadDataWithParameters<UserComplete>(sql, sqlParameters);
    }



    /*------------------------------------------------------------------------------
    -------------------------------- UPSERT USER -----------------------------------
    -------------------------------------------------------------------------------*/
    /// <summary>
    /// Inserts or updates a UserComplete entity in the database. If the user already exists,
    /// it updates their information; otherwise, it inserts a new record.
    /// </summary>
    /// <param name="user">The UserComplete entity to be inserted or updated.</param>
    /// <returns>An IActionResult indicating the success or failure of the operation.</returns>
    [HttpPut("UpsertUser")]
    public IActionResult UpsertUser(UserComplete user)
    {
        // Attempt to insert or update the user record using the reusable SQL utility
        if (_reusableSql.UpsertUser(user))
        {
            // Return a successful response if the upsert operation is successful
            return Ok();
        }

        // Throw an exception if the upsert operation fails
        throw new Exception("Failed to insert or update user.");
    }



    /*------------------------------------------------------------------------------
    ------------------------------- DELETE USER ------------------------------------
    -------------------------------------------------------------------------------*/
    /// <summary>
    /// Deletes a UserComplete entity from the database based on the provided user ID.
    /// </summary>
    /// <param name="userId">The ID of the user to be deleted.</param>
    /// <returns>An IActionResult indicating the success or failure of the deletion operation.</returns>
    [HttpDelete("DeleteUser/{userId}")]
    public IActionResult DeleteUser(int userId)
    {
        // SQL command to execute the stored procedure for deleting a user
        string sql = @"EXEC PortfolioProjectSchema.spUser_Delete @UserId = @UserIdParameter";

        // Prepare dynamic parameters for the SQL command
        DynamicParameters sqlParameters = new DynamicParameters();
        sqlParameters.Add("@UserIdParameter", userId, DbType.Int32);

        // Execute the SQL command and check if it successfully deleted the user
        if (_dapper.ExecuteSqlWithParameters(sql, sqlParameters))
        {
            // Return a successful response if the user is successfully deleted
            return Ok();
        }

        // Throw an exception if the deletion operation fails
        throw new Exception("Failed to delete user");
    }

}


