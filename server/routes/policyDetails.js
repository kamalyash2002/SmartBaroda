const express = require('express');
const router = express.Router();
const policyDetailController = require('../controllers/policyDetailController');
const auth = require('../middleware/auth');

// Create a new policy detail
router.post('/', policyDetailController.createPolicyDetail);

// Get all policy details
router.get('/', policyDetailController.getAllPolicyDetails);

// Get policy detail by ID
router.get('/:id', policyDetailController.getPolicyDetailById);

router.post('/subscribe', auth, policyDetailController.addPolicySubscribed);

module.exports = router;
