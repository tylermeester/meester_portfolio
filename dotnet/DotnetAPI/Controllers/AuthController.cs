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
        ----------------- DAPPER DATABASE CONNECTION AND CONSTRUCTOR -------------------
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
        /// <summary> Registers a new user in the system. Ensures that the email is unique and the 
        /// password and confirmation match. Upon successful registration, the user's
        /// information is added to the database.
        /// </summary>
        /// <param name="userForRegistration">DTO containing the user's registration details.</param>
        /// <returns>An IActionResult indicating the success or failure of the registration.</returns>
        [AllowAnonymous]
        [HttpPost("Register")]
        public IActionResult Register(UserForRegistrationDTO userForRegistration)
        {
            // Validate if the provided password matches the confirmation password
            if (userForRegistration.Password == userForRegistration.PasswordConfirm)
            {
                // SQL query to check for existing user with the same email
                string sqlCheckUserExists = @"
                            SELECT Email FROM PortfolioProjectSchema.Auth
                            WHERE Email = '" + userForRegistration.Email + "'";

                // Retrieve any existing users with the given email
                IEnumerable<string> existingUsers = _dapper.LoadData<string>(sqlCheckUserExists);

                // Proceed only if the email is not already registered
                if (existingUsers.Count() == 0)
                {
                    // Create a DTO for setting the user's password
                    UserForLoginDTO userForSetPassword = new UserForLoginDTO()
                    {
                        Email = userForRegistration.Email,
                        Password = userForRegistration.Password
                    };

                    // Set the password in the database and check for success
                    if (_authHelper.SetPassword(userForSetPassword))
                    {
                        // Map the registration DTO to UserComplete model
                        UserComplete userComplete = _mapper.Map<UserComplete>(userForRegistration);

                        // Set 'Active' status as it's not included in the registration DTO
                        userComplete.Active = true;

                        // Attempt to upsert the user record in the database
                        if (_reusableSql.UpsertUser(userComplete))
                        {
                            return Ok();
                        }
                        throw new Exception("Failed to register user.");
                    }
                    throw new Exception("Failed to register user.");
                };
                throw new Exception("User with this email already exists!");
            }
            throw new Exception("Passwords do not match!");
        }


        /*------------------------------------------------------------------------------
        ----------------------------- RESET PASSWORD -----------------------------------
        -------------------------------------------------------------------------------*/
        /// <summary> Updates the password for a user. The method uses the AuthHelper class 
        /// to set the new password in the database.
        /// </summary>
        /// <param name="userForSetPassword">DTO containing the user's email and new password.</param>
        /// <returns>An IActionResult indicating the success or failure of the password update.</returns>
        [HttpPut("ResetPassword")]
        public IActionResult ResetPassword(UserForLoginDTO userForSetPassword)
        {
            // Attempt to set the new password using the provided email and password
            if (_authHelper.SetPassword(userForSetPassword))
            {
                // Return a successful response if the password update is successful
                return Ok();
            }

            // Throw an exception if the password update fails
            throw new Exception("Failed to update password!");
        }


        /*------------------------------------------------------------------------------
        ---------------------------------- USER LOGIN ----------------------------------
        -------------------------------------------------------------------------------*/
        /// <summary> Authenticates a user by verifying their credentials. If the login is successful,
        /// it issues a JWT token for subsequent authenticated requests.
        /// </summary>
        /// <param name="userForLogin">DTO containing the user's email and password for login.</param>
        /// <returns>An IActionResult with a JWT token if authentication is successful.</returns>
        [AllowAnonymous]
        [HttpPost("Login")]
        public IActionResult Login(UserForLoginDTO userForLogin)
        {
            // Prepare SQL parameters to retrieve the stored password hash and salt
            string sqlForHashAndSalt = @"EXEC PortfolioProjectSchema.spLoginConfirmation_Get 
                    @Email = @EmailParam";
            DynamicParameters sqlParameters = new DynamicParameters();
            sqlParameters.Add("@EmailParam", userForLogin.Email, DbType.String);

            // Retrieve the user's hashed password and salt from the database
            UserForLoginConfirmationDTO userForLoginConfirmation = _dapper
                .LoadDataSingleWithParameters<UserForLoginConfirmationDTO>(sqlForHashAndSalt, sqlParameters);

            // Hash the input password using the retrieved salt
            byte[] passwordHash = _authHelper.GetPasswordHash(userForLogin.Password, userForLoginConfirmation.PasswordSalt);

            // Verify if the input password hash matches the stored hash
            for (int index = 0; index < passwordHash.Length; index++)
            {
                if (passwordHash[index] != userForLoginConfirmation.PasswordHash[index])
                {
                    // Return unauthorized status if the password does not match
                    return StatusCode(401, "Incorrect password!");
                }
            }

            // Issue a JWT for the authenticated user
            string userIdSql = @"
                SELECT UserId
                FROM PortfolioProjectSchema.Users
                WHERE Email = '" + userForLogin.Email + "'";
            int userId = _dapper.LoadDataSingle<int>(userIdSql);
            return Ok(new Dictionary<string, string> {
                {"token", _authHelper.CreateToken(userId)}
            });
        }


        /*------------------------------------------------------------------------------
        -------------------------------- REFRESH TOKEN ---------------------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Generates a new JWT token for the user. This method is typically used 
        /// for renewing the user's authentication token without requiring them to re-login.
        /// </summary>
        /// <returns>A new JWT token as a string.</returns>
        [HttpGet("RefreshToken")]
        public string RefreshToken()
        {
            // SQL query to retrieve the user's ID from the database
            string userIdSql = @"
                SELECT UserId
                FROM PortfolioProjectSchema.Users
                WHERE UserId = '" + User.FindFirst("userId")?.Value + "'";

            // Execute the query to fetch the user's ID
            int userId = _dapper.LoadDataSingle<int>(userIdSql);

            // Generate and return a new JWT token using the user's ID
            return _authHelper.CreateToken(userId);
        }
    }
}