using System.Data;
using Dapper;
using DotnetAPI.Data;
using DotnetAPI.Models;

namespace DotnetAPI.Helpers
{
    public class ResuableSql
    {
        /*------------------------------------------------------------------------------
        ----------------- DAPPER DATABASE CONNECTION AND CONSTRUCTOR -------------------
        -------------------------------------------------------------------------------*/
        private readonly DataContextDapper _dapper;

        public ResuableSql(IConfiguration config)
        {
            _dapper = new DataContextDapper(config);
        }


        /*------------------------------------------------------------------------------
        -------------------------------- UPSERT USER -----------------------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Inserts or updates a UserComplete entity in the database. This method determines
        /// whether to perform an insert or an update based on the presence of a UserId.
        /// </summary>
        /// <param name="user">The UserComplete entity to be inserted or updated.</param>
        /// <returns>Boolean indicating the success or failure of the operation.</returns>
        public bool UpsertUser(UserComplete user)
        {
            // Prepare SQL command to execute the stored procedure for upserting a user
            string sql = @"EXEC PortfolioProjectSchema.spUser_Upsert
                            @FirstName = @FirstNameParameter,
                            @LastName =  @LastNameParameter,
                            @Email = @EmailParameter,
                            @Gender =  @GenderParameter,
                            @Active =  @ActiveParameter,
                            @JobTitle =  @JobTitleParameter,
                            @Department =  @DepartmentParameter,
                            @Salary =  @SalaryParameter,
                            @UserId =  @UserIdParameter";

            // Set up dynamic parameters for the SQL command using the user's data
            DynamicParameters sqlParameters = new DynamicParameters();
            sqlParameters.Add("@FirstNameParameter", user.FirstName, DbType.String);
            sqlParameters.Add("@LastNameParameter", user.LastName, DbType.String);
            sqlParameters.Add("@EmailParameter", user.Email, DbType.String);
            sqlParameters.Add("@GenderParameter", user.Gender, DbType.String);
            sqlParameters.Add("@ActiveParameter", user.Active, DbType.Boolean);
            sqlParameters.Add("@JobTitleParameter", user.JobTitle, DbType.String);
            sqlParameters.Add("@DepartmentParameter", user.Department, DbType.String);
            sqlParameters.Add("@SalaryParameter", user.Salary, DbType.Decimal);
            sqlParameters.Add("@UserIdParameter", user.UserId, DbType.Int32);

            // Execute the SQL command with parameters and return the result
            return _dapper.ExecuteSqlWithParameters(sql, sqlParameters);
        }

    }
}