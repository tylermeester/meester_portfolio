using System.Data;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;
using Dapper;
using DotnetAPI.Data;
using DotnetAPI.DTOs;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using Microsoft.Data.SqlClient;
using Microsoft.IdentityModel.Tokens;

namespace DotnetAPI.Helpers
{


    public class AuthHelper
    {
        /*------------------------------------------------------------------------------
        ------------------------- DAPPER DATABASE CONNECTION  --------------------------
        -------------------------------------------------------------------------------*/

        private readonly IConfiguration _config;
        private readonly DataContextDapper _dapper;
        public AuthHelper(IConfiguration config)
        {
            _dapper = new DataContextDapper(config);
            _config = config;
        }


        /*------------------------------------------------------------------------------
        ------------------------ PASSWORD HASH HELPER METHOD ---------------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Generates a hashed password using PBKDF2 algorithm. It combines the provided password
        /// with a salt and a predefined key from the configuration to create a secure hash.
        /// </summary>
        /// <param name="password">The plain-text password to be hashed.</param>
        /// <param name="passwordSalt">The salt to be used in the hashing process.</param>
        /// <returns>A byte array representing the hashed password.</returns>
        public byte[] GetPasswordHash(string password, byte[] passwordSalt)
        {
            // Combine the provided salt with a predefined key (PasswordKey) from the configuration
            string passwordSaltPlusString = _config.GetSection("AppSettings:PasswordKey").Value +
                Convert.ToBase64String(passwordSalt);

            // Hash the password using the PBKDF2 algorithm with HMACSHA256 as the pseudo-random function
            // The combination of password, salt, and iteration count increases the security of the hash
            byte[] passwordHash = KeyDerivation.Pbkdf2(
                password: password,
                salt: Encoding.ASCII.GetBytes(passwordSaltPlusString),
                prf: KeyDerivationPrf.HMACSHA256,
                iterationCount: 1000000,
                numBytesRequested: 256 / 8
            );

            // Return the computed hash as a byte array
            return passwordHash;
        }



        /*------------------------------------------------------------------------------
        ------------------------------ JWT HELPER METHOD -------------------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Creates a JSON Web Token (JWT) for a given user ID. The token is signed and contains
        /// claims about the user, ensuring secure and authenticated access.
        /// </summary>
        /// <param name="userId">The user ID for which the token is being created.</param>
        /// <returns>A JWT as a string.</returns>
        public string CreateToken(int userId)
        {
            // Define the claim to be included in the token - the user's ID
            Claim[] claims = new Claim[] {
                new Claim("userId", userId.ToString())
            };

            // Retrieve the secret key used for signing the token from app settings
            string? tokenKeyString = _config.GetSection("AppSettings:TokenKey").Value;

            // Generate a symmetric security key from the token key string
            // This key is crucial for the integrity and security of the token
            SymmetricSecurityKey tokenKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(tokenKeyString ?? ""));

            // Create signing credentials using the security key and the HMACSHA512 algorithm
            SigningCredentials credentials = new SigningCredentials(tokenKey, SecurityAlgorithms.HmacSha512Signature);

            // Define the token properties including the subject, signing credentials, and expiration time
            SecurityTokenDescriptor descriptor = new SecurityTokenDescriptor()
            {
                Subject = new ClaimsIdentity(claims),
                SigningCredentials = credentials,
                Expires = DateTime.Now.AddDays(1) // The token will expire in 1 day
            };

            // Create a token handler to generate the JWT
            JwtSecurityTokenHandler tokenHandler = new JwtSecurityTokenHandler();

            // Create the token based on the defined properties
            SecurityToken token = tokenHandler.CreateToken(descriptor);

            // Serialize the token to a string and return it
            return tokenHandler.WriteToken(token);
        }


        /*------------------------------------------------------------------------------
        --------------------------------- SET PASSWORD ---------------------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Sets or updates the password for a given user. This method generates a new password hash and salt,
        /// and updates these credentials in the database.
        /// </summary>
        /// <param name="userForSetPassword">User data including the email and new password.</param>
        /// <returns>Boolean indicating the success or failure of the operation.</returns>
        public bool SetPassword(UserForLoginDTO userForSetPassword)
        {
            // Generate a salt for the password hashing process
            byte[] passwordSalt = new byte[128 / 8];
            using (RandomNumberGenerator rng = RandomNumberGenerator.Create())
            {
                rng.GetNonZeroBytes(passwordSalt);
            }

            // Hash the new password using the generated salt
            byte[] passwordHash = GetPasswordHash(userForSetPassword.Password, passwordSalt);

            // Prepare the SQL query to upsert the user's authentication data in the database
            string sqlAddAuth = @"EXEC PortfolioProjectSchema.spRegistration_Upsert
                                @Email = @EmailParam, 
                                @PasswordHash = @PasswordHashParam, 
                                @PasswordSalt = @PasswordSaltParam";

            // Set up dynamic parameters for the SQL query
            DynamicParameters sqlParameters = new DynamicParameters();
            sqlParameters.Add("@EmailParam", userForSetPassword.Email, DbType.String);
            sqlParameters.Add("@PasswordHashParam", passwordHash, DbType.Binary);
            sqlParameters.Add("@PasswordSaltParam", passwordSalt, DbType.Binary);

            // Execute the SQL query with parameters and return the result
            return _dapper.ExecuteSqlWithParameters(sqlAddAuth, sqlParameters);
        }

    }
}