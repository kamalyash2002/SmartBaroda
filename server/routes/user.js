const express = require('express');
const router = express.Router();
const userController = require('../controllers/userController');
const auth = require('../middleware/auth');

// Get user profile
router.get('/profile', auth, userController.getUserProfile);
router.get('/profile/:id', auth, userController.getUserProfileById);

module.exports = router;
