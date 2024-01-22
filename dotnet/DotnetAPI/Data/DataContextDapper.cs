using System.Data;
using Dapper;
using Microsoft.Data.SqlClient;

namespace DotnetAPI.Data
{
    class DataContextDapper
    {
        private readonly IConfiguration _config;

        public DataContextDapper(IConfiguration config)
        {
            _config = config; 
        }

        /*------------------------------------------------------------------------------
        --------------------------- LOAD DATA GENERIC METHOD ---------------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Generic method to load data from the database. It executes a provided SQL query
        /// and maps the results to a list of a specified type.
        /// </summary>
        /// <param name="sql">The SQL query to be executed.</param>
        /// <typeparam name="T">The type of data to be returned.</typeparam>
        /// <returns>An IEnumerable of type T representing the query results.</returns>
        public IEnumerable<T> LoadData<T>(string sql)
        {
            // Create a database connection using the connection string from appsettings.json
            using IDbConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));

            return dbConnection.Query<T>(sql);
        }


        /*------------------------------------------------------------------------------
        ------------------------ LOAD DATA SINGLE GENERIC METHOD ------------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Generic method to load a single record from the database. It executes a provided SQL query
        /// and maps the result to an object of a specified type. This method is intended for queries
        /// that are expected to return a single record.
        /// </summary>
        /// <param name="sql">The SQL query to be executed.</param>
        /// <typeparam name="T">The type of the object to be returned.</typeparam>
        /// <returns>An object of type T representing the single query result.</returns>
        public T LoadDataSingle<T>(string sql)
        {
            // Create a database connection using the connection string from appsettings.json
            using IDbConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));

            return dbConnection.QuerySingle<T>(sql);
        }


        /*------------------------------------------------------------------------------
        ------------------ LOAD DATA WITH PARAMETERS GENERIC METHOD --------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Generic method to load data from the database using specified SQL query and parameters.
        /// It executes the SQL query with provided dynamic parameters and maps the results to a list 
        /// of a specified type. This method is suitable for queries that require parameters.
        /// </summary>
        /// <param name="sql">The SQL query to be executed.</param>
        /// <param name="parameters">Dynamic parameters to be applied to the SQL query.</param>
        /// <typeparam name="T">The type of data to be returned.</typeparam>
        /// <returns>An IEnumerable of type T representing the query results with parameters applied.</returns>
        public IEnumerable<T> LoadDataWithParameters<T>(string sql, DynamicParameters parameters)
        {
            // Create a database connection using the connection string from appsettings.json
            using IDbConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));

            return dbConnection.Query<T>(sql, parameters);
        }


        /*------------------------------------------------------------------------------
        ---------------- LOAD DATA SINGLE WITH PARAMETERS GENERIC METHOD ---------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Generic method to load a single record from the database using a specified SQL query and parameters.
        /// It executes the SQL query with provided dynamic parameters and maps the result to an object of a specified type.
        /// This method is intended for queries that require parameters and are expected to return a single record.
        /// </summary>
        /// <param name="sql">The SQL query to be executed.</param>
        /// <param name="parameters">Dynamic parameters to be applied to the SQL query.</param>
        /// <typeparam name="T">The type of the object to be returned.</typeparam>
        /// <returns>An object of type T representing the single query result.</returns>
        public T LoadDataSingleWithParameters<T>(string sql, DynamicParameters parameters)
        {
            // Create a database connection using the connection string from appsettings.json
            using IDbConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));

            return dbConnection.QuerySingle<T>(sql, parameters);
        }



        /*------------------------------------------------------------------------------
        --------------------------- EXECUTE SQL GENERIC METHOD -------------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Executes a SQL command that does not return data, such as INSERT, UPDATE, or DELETE.
        /// </summary>
        /// <param name="sql">The SQL command to be executed.</param>
        /// <returns>Boolean indicating whether the operation affected any rows.</returns>
        public bool ExecuteSql(string sql)
        {
            // Create a database connection using the connection string from appsettings.json
            using IDbConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));

            // Execute the SQL command and return true if it affects one or more rows
            return dbConnection.Execute(sql) > 0;
        }

        /*------------------------------------------------------------------------------
        ------------------- EXECUTE SQL WITH ROW COUNT GENERIC METHOD ------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Executes a SQL command and returns the number of rows affected by the command.
        /// Suitable for operations where knowing the exact count of affected rows is necessary.
        /// </summary>
        /// <param name="sql">The SQL command to be executed.</param>
        /// <returns>The number of rows affected by the SQL command.</returns>
        public int ExecuteSqlWithRowCount(string sql)
        {
            // Create a database connection using the connection string from appsettings.json
            using IDbConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));

            // Execute the SQL command and return the number of rows affected
            return dbConnection.Execute(sql);
        }

        /*------------------------------------------------------------------------------
        ------------------ EXECUTE SQL WITH PARAMETERS GENERIC METHOD ------------------
        -------------------------------------------------------------------------------*/
        /// <summary>
        /// Executes a SQL command with dynamic parameters, often used for commands 
        /// that include variable input, such as INSERT or UPDATE with conditional logic.
        /// </summary>
        /// <param name="sql">The SQL command to be executed.</param>
        /// <param name="parameters">Dynamic parameters for the SQL command.</param>
        /// <returns>Boolean indicating whether the operation affected any rows.</returns>
        public bool ExecuteSqlWithParameters(string sql, DynamicParameters parameters)
        {
            // Create a database connection using the connection string from appsettings.json
            using IDbConnection dbConnection = new SqlConnection(_config.GetConnectionString("DefaultConnection"));

            // Execute the SQL command with parameters and return true if it affects one or more rows
            return dbConnection.Execute(sql, parameters) > 0;
        }


    }
}
