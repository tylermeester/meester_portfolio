using System.Data;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using DotnetAPI.Data;
using DotnetAPI.DTOs;
using DotnetAPI.Helpers;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Data.SqlClient;
using Microsoft.IdentityModel.Tokens;

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

        public AuthController(IConfiguration config)
        {
            _dapper = new DataContextDapper(config);
            _authHelper = new AuthHelper(config);
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
                    // Generate a salt for hashing the password
                    byte[] passwordSalt = new byte[128 / 8];
                    using (RandomNumberGenerator rng = RandomNumberGenerator.Create())
                    {
                        rng.GetNonZeroBytes(passwordSalt);
                    }

                    // Hash the password using PBKDF2
                    byte[] passwordHash = _authHelper.GetPasswordHash(userForRegistration.Password, passwordSalt);

                    // SQL query to insert the new user's credentials into the database
                    string sqlAddAuth = @"
                            INSERT INTO PortfolioProjectSchema.Auth ([Email],
                            [PasswordHash],
                            [PasswordSalt]) VALUES('" + userForRegistration.Email +
                            "', @PasswordHash, @PasswordSalt)";

                    // Prepare SQL parameters for secure insertion
                    List<SqlParameter> sqlParameters = new List<SqlParameter>();

                    SqlParameter passwordSaltParameter = new SqlParameter("@PasswordSalt", SqlDbType.VarBinary);
                    passwordSaltParameter.Value = passwordSalt;

                    SqlParameter passwordHashParameter = new SqlParameter("@PasswordHash", SqlDbType.VarBinary);
                    passwordHashParameter.Value = passwordHash;

                    sqlParameters.Add(passwordSaltParameter);
                    sqlParameters.Add(passwordHashParameter);

                    // Execute the SQL query and return OK if successful
                    if (_dapper.ExecuteSqlWithParameters(sqlAddAuth, sqlParameters))
                    {
                        string sqlAddUser = @"
                            INSERT INTO PortfolioProjectSchema.Users(
                                [FirstName],
                                [LastName],
                                [Email],
                                [Gender],
                                [Active]
                            )  VALUES (" +
                                "'" + userForRegistration.FirstName +
                                "', '" + userForRegistration.LastName +
                                "', '" + userForRegistration.Email +
                                "', '" + userForRegistration.Gender +
                                "', 1)";

                        if(_dapper.ExecuteSql(sqlAddUser)){
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
        ---------------------------------- USER LOGIN ----------------------------------
        -------------------------------------------------------------------------------*/
        [AllowAnonymous]
        [HttpPost("Login")]
        public IActionResult Login(UserForLoginDTO userForLogin)
        {
            // SQL query to retrieve the stored password hash and salt for the given email
            string sqlForHashAndSalt = @"SELECT
                [PasswordHash],
                [PasswordSalt] FROM PortfolioProjectSchema.Auth WHERE Email ='" +
                userForLogin.Email + "'";

            // Execute the SQL query to get the user's hashed password and salt
            // UserForLoginConfirmationDTO is expected to hold PasswordHash and PasswordSalt
            UserForLoginConfirmationDTO userForLoginConfirmation = _dapper
                .LoadDataSingle<UserForLoginConfirmationDTO>(sqlForHashAndSalt);

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

        [HttpGet("RefreshToken")]
        public string RefreshToken(){
            string userIdSql = @"
                SELECT UserId
                FROM PortfolioProjectSchema.Users
                WHERE UserId = '" + User.FindFirst("userId")?.Value + "'";

                int userId = _dapper.LoadDataSingle<int>(userIdSql);

                return _authHelper.CreateToken(userId);
        }


    }
}