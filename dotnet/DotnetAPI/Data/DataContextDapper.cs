using System.Data;
using Dapper;
using Microsoft.Data.SqlClient;

namespace DotnetAPI.Data
{
    // DataContextDapper class to handle database operations using Dapper
    class DataContextDapper
    {
        // IConfiguration field to hold configuration settings, like connection strings
        private readonly IConfiguration _config;

        // Constructor that takes IConfiguration to access application settings
        public DataContextDapper(IConfiguration config)
        {
            _config = config; // Assigning the passed configuration to the local field
        }

        // Generic method to load data from the database
        public IEnumerable<T> LoadData<T>(string sql)
        {
            // Creating a database connection using the connection string from app settings
            IDbConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));
            // Executing the SQL query using Dapper's Query method and returning the result
            return dbConnection.Query<T>(sql);
        }

        // Generic method to load a single record from the database
        public T LoadDataSingle<T>(string sql)
        {
            // Creating a database connection
            IDbConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));
            // Executing the SQL query and returning a single record
            return dbConnection.QuerySingle<T>(sql);
        }

        // Method to execute a SQL command that doesn't return data (e.g., INSERT, UPDATE, DELETE)
        public bool ExecuteSql(string sql)
        {
            // Creating and opening a database connection
            IDbConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));
            // Executing the SQL command and returning true if the number of affected rows is greater than 0
            return dbConnection.Execute(sql) > 0;
        }

        // Method to execute a SQL command and return the number of affected rows
        public int ExecuteSqlWithRowCount(string sql)
        {
            // Creating and opening a database connection
            IDbConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));
            // Executing the SQL command and returning the count of affected rows
            return dbConnection.Execute(sql);
        }


        public bool ExecuteSqlWithParameters(string sql, DynamicParameters parameters)
        {

            IDbConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));
            return dbConnection.Execute(sql, parameters) > 0;



            // SqlCommand commandWithParams = new SqlCommand(sql);

            // foreach(SqlParameter parameter in parameters){
            //     commandWithParams.Parameters.Add(parameter);
            // }

            // // Creating and opening a database connection
            // SqlConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));
            // dbConnection.Open();

            // commandWithParams.Connection = dbConnection;

            // int rowsAffected = commandWithParams.ExecuteNonQuery();

            // dbConnection.Close();



            // // Executing the SQL command and returning the count of affected rows
            // return rowsAffected > 0;
        }


        
        public IEnumerable<T> LoadDataWithParameters<T>(string sql, DynamicParameters parameters)
        {
            // Creating a database connection using the connection string from app settings
            IDbConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));
            // Executing the SQL query using Dapper's Query method and returning the result
            return dbConnection.Query<T>(sql, parameters);
        }

        // Generic method to load a single record from the database
        public T LoadDataSingleWithParameters<T>(string sql,  DynamicParameters parameters)
        {
            // Creating a database connection
            IDbConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));
            // Executing the SQL query and returning a single record
            return dbConnection.QuerySingle<T>(sql, parameters);
        }
    }
}
