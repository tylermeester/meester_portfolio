using Microsoft.AspNetCore.Mvc;
using DotnetAPI.Data;
using DotnetAPI.Models;
using DotnetAPI.DTOs;


namespace DotnetAPI.Controllers;


[ApiController]
[Route("[controller]")]
public class UserJobInfoController : ControllerBase
{

    /*------------------------------------------------------------------------------
    ------------------------- DAPPER DATABASE CONNECTION  --------------------------
    -------------------------------------------------------------------------------*/

    // Dapper context for database operations
    DataContextDapper _dapper;

    // Constructor initializes the Dapper context using the configuration provided
    public UserJobInfoController(IConfiguration config)
    {
        // Retrieves and logs the database connection string for debugging purposes
        Console.WriteLine(config.GetConnectionString("DefaultConnection"));
        _dapper = new DataContextDapper(config);
    }


    /*------------------------------------------------------------------------------
    -------------------------- GET ALL USER JOB INFO -------------------------------
    -------------------------------------------------------------------------------*/
    [HttpGet("GetAllUserJobInfo")]
    public IEnumerable<UserJobInfo> GetAllUserJobInfo()
    {
        string sql = @"
            SELECT [UserId],
                [JobTitle],
                [Department]
            FROM PortfolioProjectSchema.UserJobInfo";

        IEnumerable<UserJobInfo> allUserJobInfo = _dapper.LoadData<UserJobInfo>(sql);

        return allUserJobInfo;
    }


    /*------------------------------------------------------------------------------
    ------------------------ GET SINGLE USER JOB INFO ------------------------------
    -------------------------------------------------------------------------------*/
    [HttpGet("GetSingleUserJobInfo/{userId}")]
    public UserJobInfo GetSingleUserJobInfo(int userId)
    {
        string sql = @"
            SELECT [UserId],
                [JobTitle],
                [Department]
            FROM PortfolioProjectSchema.UserJobInfo
                WHERE UserId = " + userId.ToString();

        UserJobInfo userJobInfo = _dapper.LoadDataSingle<UserJobInfo>(sql);

        return userJobInfo;
    }



    /*------------------------------------------------------------------------------
    --------------------------- UPDATE USER JOB INFO--------------------------------
    -------------------------------------------------------------------------------*/
    [HttpPut("UpdateUserJobInfo")]
    //Not returning data, IActionResult provides a response to indicate success/failure
    public IActionResult UpdateUserJobInfo(UserJobInfo userJobInfo)
    {
        string sql = @"
        UPDATE PortfolioProjectSchema.UserJobInfo
            SET [JobTitle] = '" + userJobInfo.JobTitle +
            "', [Department] = '" + userJobInfo.Department +
            "' WHERE UserId = " + userJobInfo.UserId.ToString();

        Console.WriteLine(sql);

        if (_dapper.ExecuteSql(sql))
        {
            return Ok();
        }

        throw new Exception("Failed to update user job info.");
    }



    /*------------------------------------------------------------------------------
    ---------------------------- ADD USER JOB INFO ---------------------------------
    -------------------------------------------------------------------------------*/
    [HttpPost("AddUserJobInfo")]
    //Not returning data, IActionResult provides a response to indicate success/failure
    public IActionResult AddUserJobInfo(UserJobInfo userJobInfo)
    {

        string sql = @"INSERT INTO PortfolioProjectSchema.UserJobInfo(
                [UserId],
                [JobTitle],
                [Department]
            )  VALUES (" +
                "'" + userJobInfo.UserId +
                "', '" + userJobInfo.JobTitle +
                "', '" + userJobInfo.Department +
            "')";

        if (_dapper.ExecuteSql(sql))
        {
            return Ok();
        }

        throw new Exception("Failed to add user job info.");
    }


    /*------------------------------------------------------------------------------
    -------------------------- DELETE USER JOB INFO---------------------------------
    -------------------------------------------------------------------------------*/
    //This is the route of the request
    [HttpDelete("DeleteUserJobInfo/{userId}")]
    public IActionResult DeleteUserJobInfo(int userId)
    {
        string sql = @"
                DELETE FROM PortfolioProjectSchema.UserJobInfo
                    WHERE UserId = " + userId.ToString();

        if (_dapper.ExecuteSql(sql))
        {
            return Ok();
        }

        throw new Exception("Failed to delete user job info.");
    }


    /*------------------------------------------------------------------------------
    ------------------------- GET SINGLE USER SALARY -------------------------------
    -------------------------------------------------------------------------------*/
    [HttpGet("GetSingleUserSalary/{userId}")]
    public UserSalary GetSingleUserSalary(int userId)
    {
        string sql = @"
            SELECT [UserId],
                [Salary]
            FROM PortfolioProjectSchema.UserSalary
                WHERE UserId = " + userId.ToString();

        UserSalary userSalary = _dapper.LoadDataSingle<UserSalary>(sql);

        return userSalary;
    }

    /*------------------------------------------------------------------------------
    ----------------------------- UPDATE USER SALARY--------------------------------
    -------------------------------------------------------------------------------*/
    [HttpPut("UpdateUserSalary")]
    //Not returning data, IActionResult provides a response to indicate success/failure
    public IActionResult UpdateUserSalary(UserSalary userSalary)
    {
        string sql = @"
        UPDATE PortfolioProjectSchema.UserSalary
            SET [Salary] = '" + userSalary.Salary +
            "' WHERE UserId = " + userSalary.UserId.ToString();

        Console.WriteLine(sql);

        if (_dapper.ExecuteSql(sql))
        {
            return Ok();
        }

        throw new Exception("Failed to update user salary.");
    }

    /*------------------------------------------------------------------------------
    ------------------------------ ADD USER SALARY ---------------------------------
    -------------------------------------------------------------------------------*/
    [HttpPost("AddUserSalary")]
    //Not returning data, IActionResult provides a response to indicate success/failure
    public IActionResult AddUserSalary(UserSalary userSalary)
    {

        string sql = @"INSERT INTO PortfolioProjectSchema.UserSalary(
                [UserId],
                [Salary]
            )  VALUES (" +
                "'" + userSalary.UserId +
                "', '" + userSalary.Salary +
            "')";

        if (_dapper.ExecuteSql(sql))
        {
            return Ok();
        }

        throw new Exception("Failed to add user salary.");
    }

    /*------------------------------------------------------------------------------
    ---------------------------- DELETE USER SALARY---------------------------------
    -------------------------------------------------------------------------------*/
    //This is the route of the request
    [HttpDelete("DeleteUserSalary/{userId}")]
    public IActionResult DeleteUserSalary(int userId)
    {
        string sql = @"
                DELETE FROM PortfolioProjectSchema.UserSalary
                    WHERE UserId = " + userId.ToString();

        if (_dapper.ExecuteSql(sql))
        {
            return Ok();
        }

        throw new Exception("Failed to delete user salary.");
    }

}

