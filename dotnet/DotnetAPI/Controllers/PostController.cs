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

        private readonly DataContextDapper _dapper;

        public PostController(IConfiguration config)
        {
            _dapper = new DataContextDapper(config);
        }

        /*------------------------------------------------------------------------------
        ----------------------------------- GET POSTS ----------------------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Retrieves a collection of Post entities from the database. This method can 
        /// optionally filter posts based on post ID, user ID, and a search parameter.
        /// </summary>
        /// <param name="postId">Filter for a specific post ID (optional).</param>
        /// <param name="userId">Filter for posts by a specific user ID (optional).</param>
        /// <param name="searchParam">A search term to filter posts (optional).</param>
        /// <returns>An IEnumerable of Post entities matching the criteria.</returns>
        [HttpGet("Posts/{postId}/{userId}/{searchParam}")]
        public IEnumerable<Post> GetPosts(int postId = 0, int userId = 0, string searchParam = "None")
        {
            // Prepare SQL procedure call and parameters based on input filters
            string sql = @"EXEC PortfolioProjectSchema.spPosts_Get";
            string stringParameters = "";
            DynamicParameters sqlParameters = new DynamicParameters();

            // Add post ID to parameters if provided
            if (postId != 0)
            {
                stringParameters += ", @PostId=@PostIdParameter";
                sqlParameters.Add("@PostIdParameter", postId, DbType.Int32);
            }

            // Add user ID to parameters if provided
            if (userId != 0)
            {
                stringParameters += ", @UserId=@UserIdParameter";
                sqlParameters.Add("@UserIdParameter", userId, DbType.Int32);
            }

            // Add search parameter if provided and not default
            if (searchParam.ToLower() != "none")
            {
                stringParameters += ", @SearchValue=@SearchValueParameter";
                sqlParameters.Add("@SearchValueParameter", searchParam, DbType.String);
            }

            // Append parameters to SQL query if any were added
            if (stringParameters.Length > 0)
            {
                sql += stringParameters.Substring(1);  // Remove leading comma
            }

            // Execute the query with any applied parameters and return the result
            return _dapper.LoadDataWithParameters<Post>(sql, sqlParameters);
        }





        /*------------------------------------------------------------------------------
        -------------------------------- GET MY POSTS ----------------------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Retrieves a collection of Post entities created by the currently authenticated user.
        /// This method uses the user's ID obtained from the user claims to filter the posts.
        /// </summary>
        /// <returns>An IEnumerable of Post entities belonging to the current user.</returns>
        [HttpGet("MyPosts")]
        public IEnumerable<Post> GetMyPosts()
        {
            // SQL command to execute the stored procedure for fetching user-specific posts
            string sql = @"EXEC PortfolioProjectSchema.spPosts_Get @UserId = @UserIdParameter";

            // Prepare dynamic parameters with the current user's ID
            DynamicParameters sqlParameters = new DynamicParameters();
            sqlParameters.Add("@UserIdParameter", this.User.FindFirst("userId")?.Value, DbType.Int32);

            // Execute the query with the current user's ID as a parameter and return the result
            IEnumerable<Post> posts = _dapper.LoadDataWithParameters<Post>(sql, sqlParameters);

            return posts;
        }




        /*------------------------------------------------------------------------------
        -------------------------------- UPSERT POST------------------------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Inserts or updates a Post entity in the database. If the post already exists (identified by PostId),
        /// it updates its information; otherwise, it inserts a new post record.
        /// </summary>
        /// <param name="postToUpsert">The Post entity to be inserted or updated.</param>
        /// <returns>An IActionResult indicating the success or failure of the upsert operation.</returns>
        [HttpPut("UpsertPost")]
        public IActionResult UpsertPost(Post postToUpsert)
        {
            // SQL command to execute the stored procedure for upserting a post
            string sql = @"PortfolioProjectSchema.spPosts_Upsert
                            @UserId = @UserIdParameter,
                            @PostTitle = @PostTitleParameter,
                            @PostContent = @PostContentParameter";

            // Prepare dynamic parameters with post details
            DynamicParameters sqlParameters = new DynamicParameters();
            sqlParameters.Add("@UserIdParameter", postToUpsert.UserId, DbType.Int32);
            sqlParameters.Add("@PostTitleParameter", postToUpsert.PostTitle, DbType.String);
            sqlParameters.Add("@PostContentParameter", postToUpsert.PostContent, DbType.String);

            // Include PostId in parameters if it is an update operation
            if (postToUpsert.PostId > 0)
            {
                sql += ", @PostId=@PostIdParameter";
                sqlParameters.Add("@PostIdParameter", postToUpsert.PostId, DbType.Int32);
            }

            // Execute the SQL command with parameters and handle the response
            if (_dapper.ExecuteSqlWithParameters(sql, sqlParameters))
            {
                // Return a successful response if the upsert operation is successful
                return Ok();
            }

            // Throw an exception if the upsert operation fails
            throw new Exception("Failed to upsert post!");
        }



        /*------------------------------------------------------------------------------
        ------------------------------ DELETE POST--------------------------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Deletes a Post entity from the database based on the provided post ID. Only the post's owner can delete it.
        /// </summary>
        /// <param name="postId">The ID of the post to be deleted.</param>
        /// <returns>An IActionResult indicating the success or failure of the deletion operation.</returns>
        [HttpDelete("DeletePost/{postId}")]
        public IActionResult DeletePost(int postId)
        {
            // SQL command to execute the stored procedure for deleting a post
            string sql = @"EXEC PortfolioProjectSchema.spPost_Delete 
                            @UserId = @UserIdParameter,
                            @PostId = @PostIdParameter";

            // Prepare dynamic parameters for the SQL command including the current user's ID and post ID
            DynamicParameters sqlParameters = new DynamicParameters();
            sqlParameters.Add("@UserIdParameter", this.User.FindFirst("userId")?.Value, DbType.Int32);
            sqlParameters.Add("@PostIdParameter", postId, DbType.Int32);

            // Execute the SQL command and check if it successfully deleted the post
            if (_dapper.ExecuteSqlWithParameters(sql, sqlParameters))
            {
                // Return a successful response if the post is successfully deleted
                return Ok();
            }

            // Throw an exception if the deletion operation fails
            throw new Exception("Failed to delete post!");
        }


    }
}