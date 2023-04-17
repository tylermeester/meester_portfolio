/* eslint-disable no-unused-vars */
/* eslint-disable no-console */
const express = require('express');
const morgan = require('morgan');
const rateLimit = require('express-rate-limit');
const helmet = require('helmet');
const mongoSanitize = require('express-mongo-sanitize');
const xss = require('xss-clean');

const AppError = require('./utils/appError');
const globalErrorHandler = require('./controllers/errorController');
const tourRouter = require('./routes/tourRoutes');
const userRouter = require('./routes/userRoutes');

//////////////////////////////////////
///////// INITIALIZING APP
const app = express();

// test
//////////////////////////////////////
////////// MIDDLEWARE

// Set Security HTTP Headers
app.use(helmet()); // Make sure to use this middleware in the beginning

// Development Logging
if (process.env.NODE_ENV === 'development') {
  // Initializing morgan middleware
  app.use(morgan('dev'));
}

// Limit requests from same API
// Allows 100 requests from the same IP in one hour
const limiter = rateLimit({
  max: 100,
  windowMs: 60 * 60 * 1000, // 60 minutes * 60 seconds * 1000 miliseconds
  message: 'Too many requests from this IP, please try again in an hour',
});

app.use('/api', limiter); // Only applies it to /api

// Body Parser, Reading data from the body into req.body
// Initializing Express json middleware
app.use(
  express.json({
    limit: '10kb',
  })
);

// Data sanitization against NoSQL query injection
app.use(mongoSanitize()); // Looks at the req body, req query string, req params and filters out all $ and .

// Data sanitization against cross site scripting attacks (XSS)
app.use(xss());

// Serving static files
app.use(express.static(`${__dirname}/public`));

// Test Middleware/Middleware to add request timestamp
app.use((req, res, next) => {
  req.requestTime = new Date().toISOString();
  //  console.log(req.headers);
  next();
});

//////////////////////////////////////
///////// ROUTES

//middleware for routers
app.use('/api/v1/tours', tourRouter);
app.use('/api/v1/users', userRouter);

// This is below all of our other routes, and will only be reached in the code
// if a url is inputted that is not found in the application
app.all('*', (req, res, next) => {
  next(new AppError(`Can't find ${req.originalUrl} on this server!`, 404));
});

app.use(globalErrorHandler);

module.exports = app;
