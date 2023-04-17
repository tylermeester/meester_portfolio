/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable node/no-unpublished-require */
const mongoose = require('mongoose');
const dotenv = require('dotenv');

process.on('uncaughtException', (err) => {
  console.log(err.name, err.message);
  console.log('UNCAUGHT EXCEPTION! 🔴 Shutting Down...');
  process.exit(1);
});

dotenv.config({ path: './config.env' }); // Adds the envinronment variables defined in config.env file
const app = require('./app');

//////////////////////////////////////
///////// CONNECTING TO DATABASE
// Replacing the PASSWORD placeholder with the data held in config.env file
const DB = process.env.DATABASE.replace(
  '<PASSWORD>',
  process.env.DATABASE_PASSWORD
);

mongoose
  .connect(DB, {
    useNewUrlParser: true, // Prevents deprecation warnings
    useCreateIndex: true,
    useFindAndModify: false,
    useUnifiedTopology: true,
  })
  .then(() => {
    console.log('DB Connection Successful');
  });

//////////////////////////////////////
///////// START SERVER
const port = process.env.port || 3000;
const server = app.listen(port, () => {
  console.log(`App running on ${port}...`);
});

process.on('unhandledRejection', (err) => {
  console.log(err.name, err.message);
  console.log('UNHANDLED REJECTION! 🔴 Shutting Down...');
  server.close(() => {
    process.exit(1);
  });
});

//test
