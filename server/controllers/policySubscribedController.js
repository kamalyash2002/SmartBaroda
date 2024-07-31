const PolicySubscribed = require('../models/PolicySubscribed');

// Create a new policy subscribed
exports.createPolicySubscribed = async (req, res) => {
  try {
    const policySubscribed = new PolicySubscribed(req.body);
    await policySubscribed.save();
    res.status(201).send(policySubscribed);
  } catch (err) {
    res.status(400).send(err);
  }
};

// Get all policies subscribed
exports.getAllPoliciesSubscribed = async (req, res) => {
  try {
    const policiesSubscribed = await PolicySubscribed.find();
    res.status(200).send(policiesSubscribed);
  } catch (err) {
    res.status(400).send(err);
  }
};

// Get policy subscribed by ID
exports.getPolicySubscribedById = async (req, res) => {
  try {
    const policySubscribed = await PolicySubscribed.findById(req.params.id);
    if (!policySubscribed) return res.status(404).send();
    res.status(200).send(policySubscribed);
  } catch (err) {
    res.status(400).send(err);
  }
};
