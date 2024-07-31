const LoanIssued = require('../models/LoanIssued');
const LoanDetails = require('../models/LoanDetail');
const User = require('../models/User');

// Create a new loan issued
exports.createLoanIssued = async (req, res) => {
  try {
    const loanIssued = new LoanIssued(req.body);
    await loanIssued.save();
    res.status(201).send(loanIssued);
  } catch (err) {
    res.status(400).send(err);
  }
};

// Get all loans issued
exports.getAllLoansIssued = async (req, res) => {
  try {
    const loansIssued = await LoanIssued.find();
    res.status(200).send(loansIssued);
  } catch (err) {
    res.status(400).send(err);
  }
};

// Get loan issued by ID
exports.getLoanIssuedById = async (req, res) => {
  try {
    const loanIssued = await LoanIssued.findById(req.params.id);
    if (!loanIssued) return res.status(404).send();
    res.status(200).send(loanIssued);
  } catch (err) {
    res.status(400).send(err);
  }
};


