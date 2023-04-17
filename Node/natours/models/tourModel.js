/* eslint-disable prefer-arrow-callback */
/* eslint-disable node/no-unpublished-require */
/* eslint-disable import/no-extraneous-dependencies */
const mongoose = require('mongoose');
const slugify = require('slugify');
// eslint-disable-next-line no-unused-vars
const validator = require('validator');

const tourSchema = new mongoose.Schema(
  {
    name: {
      type: String,
      required: [true, 'A tour must have a name'],
      unique: true,
      trim: true,
      maxLength: [40, 'A tour name must hav less tha n 40 characters'],
      minLength: [10, 'A tour name must have more than 10 characters'],
      // Example of using outside validator library
      //validate: [validator.isAlpha, 'Tour name must only contain characters'],
    },
    slug: String,
    duration: {
      type: Number,
      required: [true, 'A tour must have a duration'],
    },
    maxGroupSize: {
      type: Number,
      required: [true, 'A tour must have a group size'],
    },
    difficulty: {
      type: String,
      required: [true, 'A tour must have a difficulty'],
      enum: {
        values: ['easy', 'medium', 'difficult'],
        message: 'Difficulty can be either easy, medium, or difficult',
      },
    },
    ratingsAverage: {
      type: Number,
      default: 4.5,
      min: [1, 'Rating must be between 1 and 5'],
      max: [5, 'Rating must be between 1 and 5'],
    },
    ratingsQuantity: {
      type: Number,
      default: 0,
    },
    price: {
      type: Number,
      required: [true, 'A tour must have a price'],
    },
    priceDiscount: {
      type: Number,
      validate: {
        validator: function (val) {
          // This only points to the current doc on NEW document creation, not updates
          return val < this.price;
        },
        message: 'Discount price ({VALUE}) must be less than price',
      },
    },
    summary: {
      type: String,
      trim: true,
      required: [true, 'A tour must have a description!'],
    },
    description: {
      type: String,
      trim: true,
    },
    imageCover: {
      type: String,
      required: [true, 'A tour must have a cover image'],
    },
    images: [String],
    createdAt: {
      type: Date,
      default: Date.now(),
      select: false, // Hides this field from the user
    },
    startDates: [Date],
    secretTour: {
      type: Boolean,
      default: false,
    },
  },
  {
    // Includes the virtual properties in the JSON output
    toJSON: { virtuals: true },
    toObject: { virtuals: true },
  }
);

// We used a proper function, instead of an arrow function, because it allows us
// to use the "this" keyword, which references the current document. This is virtual
// data, which means it does not get saved to the database
tourSchema.virtual('durationWeeks').get(function () {
  return this.duration / 7;
});

// Document Middleware
// (also called hook) runs only before the .save() and .create()
tourSchema.pre('save', function (next) {
  this.slug = slugify(this.name, { lower: true });
  next();
});

// Query Middleware
tourSchema.pre(/^find/, function (next) {
  // The 'this' keyword will correspond with the query, not a document
  // This is the behavior of middleware on the 'find' keyword
  // It uses a regular expression to create the hook for find and findone,
  // or any other string that starts with 'find'
  this.find({ secretTour: { $ne: true } }); // Can't use !secretTour

  this.start = Date.now();
  next();
});

tourSchema.post(/^find/, function (docs, next) {
  console.log(`Query took ${Date.now() - this.start} milliseconds`);
  next();
});

// Aggregation middleware
tourSchema.pre('aggregate', function (next) {
  // Adds a new thing to the $match part of the aggregate() pipeline that is found in
  // getTourStat. With this aggregation middleware added, it will now filter out documents
  // which hav a secretTour value of true
  this.pipeline().unshift({ $match: { secretTour: { $ne: true } } });
  console.log(this);
  next();
});

const Tour = mongoose.model('Tour', tourSchema);

module.exports = Tour;
