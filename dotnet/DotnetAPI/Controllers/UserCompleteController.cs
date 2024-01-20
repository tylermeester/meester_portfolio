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
    ------------------------- DAPPER DATABASE CONNECTION  --------------------------
    -------------------------------------------------------------------------------*/

    // Dapper context for database operations
    private readonly DataContextDapper _dapper;

    private readonly ResuableSql _reusableSql;

    // Constructor initializes the Dapper context using the configuration provided
    public UserCompleteController(IConfiguration config)
    {
        _dapper = new DataContextDapper(config);
        _reusableSql = new ResuableSql(config);
    }


    /*------------------------------------------------------------------------------
    --------------------------------- GET USERS ------------------------------------
    -------------------------------------------------------------------------------*/
    [HttpGet("GetUsers/{userId}/{isActive}")]
    public IEnumerable<UserComplete> GetUsers(int userId, bool isActive)
    {
        //Default SQL query is to get all users
        string sql = @"EXEC PortfolioProjectSchema.spUsers_Get";
        string stringParameters = "";

        DynamicParameters sqlParameters = new DynamicParameters();



        //If there is an input userId, add it to the SQL query
        if (userId != 0)
        {
            stringParameters += ", @UserId=@UserIdParameter";
            sqlParameters.Add("@UserIdParameter", userId, DbType.Int32);
        }

        if (isActive)
        {
            stringParameters += ", @Active=@ActiveParameter";
            sqlParameters.Add("@ActiveParameter", isActive, DbType.Boolean);

        }

        if (stringParameters.Length > 0){

        sql += stringParameters.Substring(1);

        }


        IEnumerable<UserComplete> users = _dapper.LoadDataWithParameters<UserComplete>(sql, sqlParameters);
        return users;
    }


    /*------------------------------------------------------------------------------
    -------------------------------- UPSERT USER -------------------------------------
    -------------------------------------------------------------------------------*/
    [HttpPut("UpsertUser")]
    //Not returning data, IActionResult provides a response to indicate success/failure
    public IActionResult UpsertUser(UserComplete user)
    {

        if (_reusableSql.UpsertUser(user))
        {
            return Ok();
        }

        throw new Exception("Failed to insert or update user.");
    }




    /*------------------------------------------------------------------------------
    ------------------------------- DELETE USER ------------------------------------
    -------------------------------------------------------------------------------*/
    //This is the route of the request
    [HttpDelete("DeleteUser/{userId}")]
    public IActionResult DeleteUser(int userId)
    {
        string sql = @"EXEC PortfolioProjectSchema.spUser_Delete
                @UserId = @UserIdParameter";

        DynamicParameters sqlParameters = new DynamicParameters();
        sqlParameters.Add("@UserIdParameter", userId, DbType.Int32);

        if (_dapper.ExecuteSqlWithParameters(sql, sqlParameters))
        {
            return Ok();
        }

        throw new Exception("Failed to delete user");
    }
}


