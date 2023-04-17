/* eslint-disable no-unused-vars */
/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable node/no-unpublished-require */
const fs = require('fs');
const mongoose = require('mongoose');
const dotenv = require('dotenv');
const tourModel = require('../../models/tourModel');
const Tour = require('../../models/tourModel');

dotenv.config({ path: './config.env' }); // Adds the envinronment variables defined in config.env file

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

// Read JSON file
const tours = JSON.parse(
  fs.readFileSync(`${__dirname}/tours-simple.json`, 'utf-8')
);

// Import data into database
const importData = async () => {
  try {
    await Tour.create(tours);
    console.log('Data successfully loaded');
  } catch (err) {
    console.log(err);
  }
  process.exit();
};

// Delete all data from database
const deleteData = async () => {
  try {
    await Tour.deleteMany();
    console.log('Data successfully deleted');
  } catch (err) {
    console.log(err);
  }
  process.exit();
};

if (process.argv[2] === '--import') {
  importData();
} else if (process.argv[2] === '--delete') {
  deleteData();
}
