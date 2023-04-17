const express = require('express');
const tourController = require('../controllers/tourController');
const authController = require('../controllers/authController');

// Router
const router = express.Router();

//router.param('id', tourController.checkID);
// routers call on middleware
// Aliasing
router
  .route('/top-5-cheap')
  .get(tourController.aliasTopTours, tourController.getAllTours);

router.route('/tour-stats').get(tourController.getTourStats);
router.route('/monthly-plan/:year').get(tourController.getMonthlyPlan);

router
  .route('/')
  // authController authenticates whether user has access, and then the next middleware function is called to actually get the tours
  .get(authController.protect, tourController.getAllTours)
  .post(tourController.createTour);

router
  .route('/:id')
  .get(tourController.getTour)
  .patch(tourController.updateTour)
  .delete(
    authController.protect,
    authController.restrictTo('admin', 'lead-guide'),
    tourController.deleteTour
  );

module.exports = router;
