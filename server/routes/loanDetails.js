const express = require('express');
const router = express.Router();
const loanDetailController = require('../controllers/loanDetailController');
const auth = require('../middleware/auth');

// Create a new loan detail
router.post('/', loanDetailController.createLoanDetail);

// Get all loan details
router.get('/', loanDetailController.getAllLoanDetails);

// Get loan detail by ID
router.get('/:id', loanDetailController.getLoanDetailById);

router.post('/issue', auth, loanDetailController.issueLoan);

module.exports = router;
