using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;
using DotnetAPI.Data;
using Microsoft.AspNetCore.Cryptography.KeyDerivation;
using Microsoft.IdentityModel.Tokens;

namespace DotnetAPI.Helpers
{


    public class AuthHelper
    {

        private readonly IConfiguration _config;

        public AuthHelper(IConfiguration config)
        {
            _config = config;
        }


        /*------------------------------------------------------------------------------
        ---------------------- PASSWORD HASH HELPER FUNCTION ---------------------------
        -------------------------------------------------------------------------------*/
        public byte[] GetPasswordHash(string password, byte[] passwordSalt)
        {
            // Combine the salt with a predefined key from the configuration
            string passwordSaltPlusString = _config.GetSection("AppSettings:PasswordKey").Value +
                Convert.ToBase64String(passwordSalt);

            // Hash the password using PBKDF2
            byte[] passwordHash = KeyDerivation.Pbkdf2(
                password: password,
                salt: Encoding.ASCII.GetBytes(passwordSaltPlusString),
                prf: KeyDerivationPrf.HMACSHA256,
                iterationCount: 1000000,
                numBytesRequested: 256 / 8
            );

            return passwordHash;
        }


        /*------------------------------------------------------------------------------
        ----------------------------- JWT HELPER FUNCTION ------------------------------
        -------------------------------------------------------------------------------*/
        public string CreateToken(int userId)
        {
            // Creating claims for the token. A claim is a statement about the user.
            // Here, we're adding the user's ID as a claim.
            Claim[] claims = new Claim[] {
                new Claim("userId", userId.ToString())
            };

            // Retrieving the token key from app settings, which is used for signing the token.
            // The key is a secret string that helps ensure the token's integrity and security.
            string? tokenKeyString = _config.GetSection("AppSettings:TokenKey").Value;

            // Generating a symmetric security key from the token key string.
            // This key will be used to sign the JWT, ensuring that it hasn't been tampered with.
            SymmetricSecurityKey tokenKey = new SymmetricSecurityKey(
                    Encoding.UTF8.GetBytes(
                        tokenKeyString != null ? tokenKeyString : ""
                    )
                );

            // Creating signing credentials with the security key and specifying the algorithm.
            // HMACSHA512 is used for signing the token.
            SigningCredentials credentials = new SigningCredentials(
                tokenKey,
                SecurityAlgorithms.HmacSha512Signature);

            // Defining the token's properties using a SecurityTokenDescriptor.
            // It includes the claims, signing credentials, and the token's expiry.
            SecurityTokenDescriptor descriptor = new SecurityTokenDescriptor()
            {

                Subject = new ClaimsIdentity(claims),
                SigningCredentials = credentials,
                Expires = DateTime.Now.AddDays(1) // Token will expire in 1 day
            };

            // Creating a token handler - an object used to create and validate JWT tokens.
            JwtSecurityTokenHandler tokenHandler = new JwtSecurityTokenHandler();

            // Creating the token based on the descriptor's properties.
            SecurityToken token = tokenHandler.CreateToken(descriptor);

            // Returning the serialized token as a string.
            return tokenHandler.WriteToken(token);
        }



    }





}