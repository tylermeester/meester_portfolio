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
        -------------------------------- GET ALL POSTS ---------------------------------
        -------------------------------------------------------------------------------*/
        [HttpGet("Posts")]
        public IEnumerable<Post> GetPosts()
        {

            string sql = @"
                SELECT [PostId],
                    [UserId],
                    [PostTitle],
                    [PostContent],
                    [PostCreated],
                    [PostUpdated] 
                FROM PortfolioProjectSchema.Posts";

            IEnumerable<Post> posts = _dapper.LoadData<Post>(sql);

            return posts;
        }

        /*------------------------------------------------------------------------------
        ------------------------------ GET SINGLE POST----------------------------------
        -------------------------------------------------------------------------------*/
        [HttpGet("GetSinglePost/{postId}")]
        public Post GetSinglePost(int postId)
        {
            string sql = @"
                SELECT [PostId],
                    [UserId],
                    [PostTitle],
                    [PostContent],
                    [PostCreated],
                    [PostUpdated] 
                FROM PortfolioProjectSchema.Posts
                WHERE [PostId] = " + postId.ToString();

            Post post = _dapper.LoadDataSingle<Post>(sql);

            return post;
        }

        /*------------------------------------------------------------------------------
        ---------------------------- GET POSTS BY USER----------------------------------
        -------------------------------------------------------------------------------*/
        [HttpGet("GetPostsByUser/{userId}")]
        public IEnumerable<Post> GetPostsByUser(int userId)
        {
            string sql = @"
                SELECT [PostId],
                    [UserId],
                    [PostTitle],
                    [PostContent],
                    [PostCreated],
                    [PostUpdated] 
                FROM PortfolioProjectSchema.Posts
                WHERE [UserId] = " + userId.ToString();

            IEnumerable<Post> posts = _dapper.LoadData<Post>(sql);

            return posts;
        }

        /*------------------------------------------------------------------------------
        -------------------------------- GET MY POSTS ----------------------------------
        -------------------------------------------------------------------------------*/
        [HttpGet("GetMyPosts")]
        public IEnumerable<Post> GetMyPosts()
        {
            string sql = @"
                SELECT [PostId],
                    [UserId],
                    [PostTitle],
                    [PostContent],
                    [PostCreated],
                    [PostUpdated] 
                FROM PortfolioProjectSchema.Posts
                WHERE [UserId] = " + this.User.FindFirst("userId")?.Value;

            IEnumerable<Post> posts = _dapper.LoadData<Post>(sql);

            return posts;
        }

        /*------------------------------------------------------------------------------
        --------------------------------- SEARCH POSTS ---------------------------------
        -------------------------------------------------------------------------------*/
        [HttpGet("SearchPosts/{searchParam}")]
        public IEnumerable<Post> SearchPosts(string searchParam)
        {

            string sql = @"
                SELECT [PostId],
                    [UserId],
                    [PostTitle],
                    [PostContent],
                    [PostCreated],
                    [PostUpdated] 
                FROM PortfolioProjectSchema.Posts
                        WHERE PostTitle LIKE '%" + searchParam + "%'"
                        + " OR PostContent LIKE '%" + searchParam + "%'";

            IEnumerable<Post> posts = _dapper.LoadData<Post>(sql);

            return posts;
        }


        /*------------------------------------------------------------------------------
        -------------------------------- ADD POST---------------------------------------
        -------------------------------------------------------------------------------*/
        [HttpPost("Post")]
        public IActionResult AddPost(PostToAddDTO postToAdd)
        {

            string sql = @"
                INSERT INTO PortfolioProjectSchema.Posts(
                    [UserId],
                    [PostTitle],
                    [PostContent],
                    [PostCreated],
                    [PostUpdated]) VALUES(" + this.User.FindFirst("userId")?.Value
                    + ", '" + postToAdd.PostTitle
                    + "','" + postToAdd.PostContent
                    + "', GETDATE(), GETDATE() )";

            if (_dapper.ExecuteSql(sql))
            {
                return Ok();
            }

            throw new Exception("Failed to create new post!");
        }


        /*------------------------------------------------------------------------------
        -------------------------------- EDIT POST--------------------------------------
        -------------------------------------------------------------------------------*/
        [HttpPut("Post")]
        public IActionResult EditPost(PostToEditDTO postToEdit)
        {

            string sql = @"
                UPDATE PortfolioProjectSchema.Posts
                    SET PostContent = '" + postToEdit.PostContent +
                    "', PostTitle = '" + postToEdit.PostTitle +
                    "', PostUpdated = GETDATE() " +
                        "WHERE PostId = " +
                        postToEdit.PostId.ToString() +
                        "AND UserId = " + this.User.FindFirst("userId")?.Value;

            if (_dapper.ExecuteSql(sql))
            {
                return Ok();
            }

            throw new Exception("Failed to edit post!");
        }

        /*------------------------------------------------------------------------------
        ------------------------------ DELETE POST--------------------------------------
        -------------------------------------------------------------------------------*/
        [HttpDelete("Post/{postId}")]
        public IActionResult DeletePost(int postId)
        {

            string sql = @"DELETE FROM PortfolioProjectSchema.Posts
                            WHERE PostId = " + postId.ToString() + 
                            "AND UserId = " + this.User.FindFirst("userId")?.Value;

            if (_dapper.ExecuteSql(sql))
            {
                return Ok();
            } 

            throw new Exception("Failed to delete post!");
        }

    }
}