DROP TABLE IF EXISTS PortfolioProjectSchema.Users;

-- IF OBJECT_ID('PortfolioProjectSchema.Users') IS NOT NULL
--     DROP TABLE PortfolioProjectSchema.Users;

CREATE TABLE PortfolioProjectSchema.Users
(
    UserId INT IDENTITY(1, 1) PRIMARY KEY
    , FirstName NVARCHAR(50)
    , LastName NVARCHAR(50)
    , Email NVARCHAR(50)
    , Gender NVARCHAR(50)
    , Active BIT
);

DROP TABLE IF EXISTS PortfolioProjectSchema.UserSalary;

-- IF OBJECT_ID('PortfolioProjectSchema.UserSalary') IS NOT NULL
--     DROP TABLE PortfolioProjectSchema.UserSalary;

CREATE TABLE PortfolioProjectSchema.UserSalary
(
    UserId INT
    , Salary DECIMAL(18, 4)
);

DROP TABLE IF EXISTS PortfolioProjectSchema.UserJobInfo;

-- IF OBJECT_ID('PortfolioProjectSchema.UserJobInfo') IS NOT NULL
--     DROP TABLE PortfolioProjectSchema.UserJobInfo;

CREATE TABLE PortfolioProjectSchema.UserJobInfo
(
    UserId INT
    , JobTitle NVARCHAR(50)
    , Department NVARCHAR(50),
);

-- USE PortfolioProjectDatabase;
-- GO

-- SELECT  [UserId]
--         , [FirstName]
--         , [LastName]
--         , [Email]
--         , [Gender]
--         , [Active]
--   FROM  PortfolioProjectSchema.Users;

-- SELECT  [UserId]
--         , [Salary]
--   FROM  PortfolioProjectSchema.UserSalary;

-- SELECT  [UserId]
--         , [JobTitle]
--         , [Department]
--   FROM  PortfolioProjectSchema.UserJobInfo;