using System.Data;
using Dapper;
using DotnetAPI.Data;
using DotnetAPI.DTOs;
using DotnetAPI.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace DotnetAPI.Controllers
{
    [Authorize]
    [ApiController]
    [Route("[controller]")]
    public class PostController : ControllerBase
    {

        /*------------------------------------------------------------------------------
        ------------------------- DAPPER DATABASE CONNECTION  --------------------------
        -------------------------------------------------------------------------------*/

        // Dapper context for database operations
        private readonly DataContextDapper _dapper;

        // Constructor initializes the Dapper context using the conf iguration provided
        public PostController(IConfiguration config)
        {
            _dapper = new DataContextDapper(config);
        }

        /*------------------------------------------------------------------------------
        ----------------------------------- GET POSTS ----------------------------------
        -------------------------------------------------------------------------------*/
        [HttpGet("Posts/{postId}/{userId}/{searchParam}")]
        public IEnumerable<Post> GetPosts(int postId = 0, int userId = 0, string searchParam = "None")
        {

            string sql = @"EXEC PortfolioProjectSchema.spPosts_Get";
            string stringParameters = "";

            DynamicParameters sqlParameters = new DynamicParameters();


            if (postId != 0)
            {
                stringParameters += ", @PostId=@PostIdParameter";
                sqlParameters.Add("@PostIdParameter", postId, DbType.Int32);
            }

            if (userId != 0)
            {
                stringParameters += ", @UserId=@UserIdParameter";
                sqlParameters.Add("@UserIdParameter", userId, DbType.Int32);

            }
            
            if (searchParam.ToLower() != "none")
            {
                stringParameters += ", @SearchValue=@SearchValueParameter";
                sqlParameters.Add("@SearchValueParameter", searchParam, DbType.String);

            }

            if (stringParameters.Length > 0){
                sql += stringParameters.Substring(1);
            }


            IEnumerable<Post> posts = _dapper.LoadDataWithParameters<Post>(sql, sqlParameters);

            return posts;
        }




        /*------------------------------------------------------------------------------
        -------------------------------- GET MY POSTS ----------------------------------
        -------------------------------------------------------------------------------*/
        [HttpGet("MyPosts")]
        public IEnumerable<Post> GetMyPosts()
        {
            string sql = @"EXEC PortfolioProjectSchema.spPosts_Get 
                    @UserId = @UserIdParameter";

            DynamicParameters sqlParameters = new DynamicParameters();
            sqlParameters.Add("@UserIdParameter", this.User.FindFirst("userId")?.Value, DbType.Int32);

            IEnumerable<Post> posts = _dapper.LoadDataWithParameters<Post>(sql, sqlParameters);

            return posts;
        }



        /*------------------------------------------------------------------------------
        -------------------------------- UPSERT POST------------------------------------
        -------------------------------------------------------------------------------*/
        [HttpPut("UpsertPost")]
        public IActionResult UpsertPost(Post postToUpsert)
        {

            string sql = @"PortfolioProjectSchema.spPosts_Upsert
                        @UserId = @UserIdParameter,
                        @PostTitle = @PostTitleParameter,
                        @PostContent = @PostContentParameter";

            DynamicParameters sqlParameters = new DynamicParameters();

            sqlParameters.Add("@UserIdParameter", postToUpsert.UserId, DbType.Int32);
            sqlParameters.Add("@PostTitleParameter", postToUpsert.PostTitle, DbType.String);
            sqlParameters.Add("@PostContentParameter", postToUpsert.PostContent, DbType.String);

            if (postToUpsert.PostId > 0)
            {
                sql += ", @PostId=@PostIdParameter";
                sqlParameters.Add("@PostIdParameter", postToUpsert.PostId, DbType.Int32);
            }            

            if (_dapper.ExecuteSqlWithParameters(sql, sqlParameters))
            
            {
                return Ok();
            }

            throw new Exception("Failed to upsert post!");
        }


        /*------------------------------------------------------------------------------
        ------------------------------ DELETE POST--------------------------------------
        -------------------------------------------------------------------------------*/
        [HttpDelete("DeletePost/{postId}")]
        public IActionResult DeletePost(int postId)
        {

            string sql = @"EXEC PortfolioProjectSchema.spPost_Delete 
                        @UserId = @UserIdParameter,
                        @PostId = @PostIdParameter";
                
            DynamicParameters sqlParameters = new DynamicParameters();
            sqlParameters.Add("@UserIdParameter", this.User.FindFirst("userId")?.Value, DbType.Int32);
            sqlParameters.Add("@PostIdParameter", postId, DbType.Int32);

            if (_dapper.ExecuteSqlWithParameters(sql, sqlParameters))
            {
                return Ok();
            } 

            throw new Exception("Failed to delete post!");
        }

    }
}