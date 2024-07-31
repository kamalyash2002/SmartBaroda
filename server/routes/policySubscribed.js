const express = require('express');
const router = express.Router();
const policySubscribedController = require('../controllers/policySubscribedController');

// Create a new policy subscribed
router.post('/', policySubscribedController.createPolicySubscribed);

// Get all policies subscribed
router.get('/', policySubscribedController.getAllPoliciesSubscribed);

// Get policy subscribed by ID
router.get('/:id', policySubscribedController.getPolicySubscribedById);

module.exports = router;
