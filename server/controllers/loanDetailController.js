const LoanDetail = require('../models/LoanDetail');
const LoanIssued = require('../models/LoanIssued');
const User = require('../models/User');

// Create a new loan detail
exports.createLoanDetail = async (req, res) => {
  try {
    const loanDetail = new LoanDetail(req.body);
    await loanDetail.save();
    res.status(201).send(loanDetail);
  } catch (err) {
    res.status(400).send(err);
  }
};

// Get all loan details
exports.getAllLoanDetails = async (req, res) => {
  try {
    const loanDetails = await LoanDetail.find();
    res.status(200).send(loanDetails);
  } catch (err) {
    res.status(400).send(err);
  }
};

// Get loan detail by ID
exports.getLoanDetailById = async (req, res) => {
  try {
    const loanDetail = await LoanDetail.findById(req.params.id);
    if (!loanDetail) return res.status(404).send();
    res.status(200).send(loanDetail);
  } catch (err) {
    res.status(400).send(err);
  }
};

exports.issueLoan = async (req, res) => {
  try {
    const { desc, recieverAccNo, type, amount, loanId, interest, status } = req.body;

    const receiver = await User.findOne({ accNo: recieverAccNo });
    if (!receiver) {
      return res.status(404).json({ msg: 'Receiver not found' });
    }

    const loanDetails = await LoanDetail.findById(loanId);
    if (!loanDetails) {
      return res.status(404).json({ msg: 'Loan details not found' });
    }

    const loanIssued = new LoanIssued({
      desc,
      recieverAccNo,
      type,
      amount,
      loanId,
      interest,
      status
    });

    await loanIssued.save();

    res.status(201).json(loanIssued);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};