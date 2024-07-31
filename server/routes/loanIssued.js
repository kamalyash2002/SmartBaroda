const express = require('express');
const router = express.Router();
const loanIssuedController = require('../controllers/loanIssuedController');

// Create a new loan issued
router.post('/', loanIssuedController.createLoanIssued);

// Get all loans issued
router.get('/', loanIssuedController.getAllLoansIssued);

// Get loan issued by ID
router.get('/:id', loanIssuedController.getLoanIssuedById);

module.exports = router;
