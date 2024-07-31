const express = require('express');
const { check } = require('express-validator');
const router = express.Router();
const authController = require('../controllers/authController');

// Register route
router.post('/register', [
    check('accNo', 'Account number is required').not().isEmpty(),
    check('username', 'Username is required').not().isEmpty(),
    check('panCard', 'Pan card is required').not().isEmpty(),
    check('cibilScore', 'Cibil score is required').not().isEmpty(),
    check('type', 'Type is required').not().isEmpty(),
    check('password', 'Password is required').isLength({ min: 6 })
], authController.register);

// Login route
router.post('/login', [
    check('accNo', 'Account number is required').not().isEmpty(),
    check('password', 'Password is required').exists()
], authController.login);

module.exports = router;
