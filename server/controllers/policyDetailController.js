const PolicyDetail = require('../models/PolicyDetail');
const PoliciesSubscribed = require('../models/PolicySubscribed');
const User = require('../models/User');

// Create a new policy detail
exports.createPolicyDetail = async (req, res) => {
  try {
    const policyDetail = new PolicyDetail(req.body);
    await policyDetail.save();
    res.status(201).send(policyDetail);
  } catch (err) {
    res.status(400).send(err);
  }
};

// Get all policy details
exports.getAllPolicyDetails = async (req, res) => {
  try {
    const policyDetails = await PolicyDetail.find();
    res.status(200).send(policyDetails);
  } catch (err) {
    res.status(400).send(err);
  }
};

// Get policy detail by ID
exports.getPolicyDetailById = async (req, res) => {
  try {
    const policyDetail = await PolicyDetail.findById(req.params.id);
    if (!policyDetail) return res.status(404).send();
    res.status(200).send(policyDetail);
  } catch (err) {
    res.status(400).send(err);
  }
};

exports.addPolicySubscribed = async (req, res) => {
  try {
    const { desc, policyId, premium, type } = req.body;

    // Get the accNo of the user from the authenticated user
    const recieverAccNo = req.user.accNo;

    // Find the user and policy in the database
    const user = await User.findOne({ accNo: recieverAccNo });
    const policy = await PolicyDetail.findById(policyId);

    if (!user) {
      return res.status(404).json({ msg: 'User not found' });
    }

    if (!policy) {
      return res.status(404).json({ msg: 'Policy not found' });
    }

    // Create the policy subscription record
    const policySubscribed = new PoliciesSubscribed({
      desc,
      recieverAccNo,
      type,
      premium,
      policyId
    });

    // Save the policy subscription record
    await policySubscribed.save();

    // Optionally, update the user's policy subscriptions
    user.policiesSubscribed.push(policySubscribed._id);
    await user.save();

    res.status(201).json(policySubscribed);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};