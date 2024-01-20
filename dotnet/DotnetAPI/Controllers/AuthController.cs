using System.Data;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using Dapper;
using DotnetAPI.Data;
using DotnetAPI.DTOs;
using DotnetAPI.Helpers;
using DotnetAPI.Models;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Components.Forms.Mapping;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Data.SqlClient;
using Microsoft.IdentityModel.Tokens;
using AutoMapper;

namespace DotnetAPI.Controllers
{
    [Authorize]
    [ApiController]
    [Route("[controller]")]
    public class AuthController : ControllerBase
    {

        /*------------------------------------------------------------------------------
        ------------------------- DAPPER DATABASE CONNECTION  --------------------------
        -------------------------------------------------------------------------------*/
        private readonly DataContextDapper _dapper;
        private readonly AuthHelper _authHelper;
        private readonly ResuableSql _reusableSql;
        private readonly IMapper _mapper;


        public AuthController(IConfiguration config)
        {
            _dapper = new DataContextDapper(config);
            _authHelper = new AuthHelper(config);
            _reusableSql = new ResuableSql(config);
            _mapper = new Mapper(new MapperConfiguration(cfg => 
            {
                cfg.CreateMap<UserForRegistrationDTO, UserComplete>();
            }));

        }



        /*------------------------------------------------------------------------------
        ----------------------------- REGISTER NEW USER --------------------------------
        -------------------------------------------------------------------------------*/
        [AllowAnonymous]
        [HttpPost("Register")]
        public IActionResult Register(UserForRegistrationDTO userForRegistration)
        {
            // Check if the provided password and password confirmation match
            if (userForRegistration.Password == userForRegistration.PasswordConfirm)
            {
                // SQL query to check if the user already exists in the database
                string sqlCheckUserExists = @"
                            SELECT Email FROM PortfolioProjectSchema.Auth
                            WHERE Email = '" + userForRegistration.Email + "'";

                // Fetch a list of existing users with the same email
                IEnumerable<string> existingUsers = _dapper.LoadData<string>(sqlCheckUserExists);

                // Check if the email is not already in use
                if (existingUsers.Count() == 0)
                {
                    UserForLoginDTO userForSetPassword = new UserForLoginDTO()
                    {
                        Email = userForRegistration.Email,
                        Password = userForRegistration.Password
                    };

                    // Execute the SQL query and return OK if successful
                    if (_authHelper.SetPassword(userForSetPassword))
                    {

                        UserComplete userComplete = _mapper.Map<UserComplete>(userForRegistration);
                        
                        //Because active is not in the UserForRegistrationDTO we set it manually
                        userComplete.Active = true; 

                        if (_reusableSql.UpsertUser(userComplete))
                        {
                            return Ok();
                        }
                        throw new Exception("Failed to register user.");

                    }

                    // Throw an exception if user registration fails
                    throw new Exception("Failed to register user.");
                };

                // Throw an exception if the email is already in use
                throw new Exception("User with this email already exists!");
            }

            // Throw an exception if the passwords do not match
            throw new Exception("Passwords do not match!");
        }

        /*------------------------------------------------------------------------------
        ------------------------------- RESET PASSWORD ---------------------------------
        -------------------------------------------------------------------------------*/
        [HttpPut("ResetPassword")]
        public IActionResult ResetPassword(UserForLoginDTO userForSetPassword)
        {
            if (_authHelper.SetPassword(userForSetPassword))
            {
                return Ok();
            }

            throw new Exception("Failed to update password!");
        }


        /*------------------------------------------------------------------------------
        ---------------------------------- USER LOGIN ----------------------------------
        -------------------------------------------------------------------------------*/
        [AllowAnonymous]
        [HttpPost("Login")]
        public IActionResult Login(UserForLoginDTO userForLogin)
        {
            // SQL query to retrieve the stored password hash and salt for the given email
            string sqlForHashAndSalt = @"EXEC PortfolioProjectSchema.spLoginConfirmation_Get 
                @Email = @EmailParam";

            DynamicParameters sqlParameters = new DynamicParameters();

            // SqlParameter emailParameter = new SqlParameter("@EmailParam", SqlDbType.VarChar);
            // emailParameter.Value = userForLogin.Email;
            // sqlParameters.Add(emailParameter);

            sqlParameters.Add("@EmailParam", userForLogin.Email, DbType.String);


            // Execute the SQL query to get the user's hashed password and salt
            // UserForLoginConfirmationDTO is expected to hold PasswordHash and PasswordSalt
            UserForLoginConfirmationDTO userForLoginConfirmation = _dapper
                .LoadDataSingleWithParameters<UserForLoginConfirmationDTO>(sqlForHashAndSalt, sqlParameters);

            // Hash the provided password using the same salt that was used to hash the stored password
            byte[] passwordHash = _authHelper.GetPasswordHash(userForLogin.Password, userForLoginConfirmation.PasswordSalt);

            // Compare the newly generated hash with the stored hash, the should be the same
            for (int index = 0; index < passwordHash.Length; index++)
            {
                // If any byte is different, return a 401 Unauthorized status with an error message
                if (passwordHash[index] != userForLoginConfirmation.PasswordHash[index])
                {
                    return StatusCode(401, "Incorrect password!");
                }
            }

            //Issuing a JWT so that they do not need to login again within 24 hours
            // SQL query to retrieve the user's ID based on their email
            string userIdSql = @"
                SELECT UserId
                FROM PortfolioProjectSchema.Users
                WHERE Email = '" + userForLogin.Email + "'";

            // Execute the SQL query to get the user's ID
            int userId = _dapper.LoadDataSingle<int>(userIdSql);

            // Create a token for the authenticated user and return it in the response
            return Ok(new Dictionary<string, string> {
                {"token", _authHelper.CreateToken(userId)}
            });
        }

        /*------------------------------------------------------------------------------
        -------------------------------- REFRESH TOKEN ---------------------------------
        -------------------------------------------------------------------------------*/

        [HttpGet("RefreshToken")]
        public string RefreshToken()
        {
            string userIdSql = @"
                SELECT UserId
                FROM PortfolioProjectSchema.Users
                WHERE UserId = '" + User.FindFirst("userId")?.Value + "'";

            int userId = _dapper.LoadDataSingle<int>(userIdSql);

            return _authHelper.CreateToken(userId);
        }


    }
}