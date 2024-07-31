const User = require('../models/User');
const Transaction = require('../models/Transaction');
const LoansIssued = require('../models/LoanIssued');
const PoliciesSubscribed = require('../models/PolicySubscribed');
const GST = require('../models/GST');

exports.getUserProfile = async (req, res) => {
  try {
    const accNo = req.user.accNo;

    // Find the user by accNo
    const user = await User.findOne({ accNo });

    if (!user) return res.status(404).send('User not found');

    // Find issued loans for the user
    const issuedLoans = await LoansIssued.find({ recieverAccNo: accNo });

    // Fetch transactions manually
    const transactions = await Transaction.find({
      $or: [{ senderAccNo: accNo }, { recieverAccNo: accNo }]
    });

    // Format transactions to include a flag for sender
    const formattedTransactions = transactions.map(transaction => ({
      ...transaction._doc,
      isSender: transaction.senderAccNo === accNo
    }));

    // Fetch seller details if GSTIN is available
    let sellerDetails = null;
    if (user.GSTIN) {
      sellerDetails = await GST.findOne({ gstNo: user.GSTIN });
    }

    res.status(200).json({ 
      ...user._doc, 
      transactions: formattedTransactions, 
      loans: issuedLoans, 
      sellerDetails 
    });
  } catch (err) {
    res.status(500).send(err.message);
  }
};

exports.getUserProfileById = async (req, res) => {
  try {
    const accNo = req.params.id;

    // Find the user by accNo
    const user = await User.findOne({ accNo });
    if (!user) return res.status(404).send('User not found');

    // Fetch GST details if the user has a GSTIN
    let sellerDetails = null;
    if (user.GSTIN) {
      sellerDetails = await GST.findOne({ gstNo: user.GSTIN });
    }

    res.status(200).json({
      user,
      sellerDetails
    });
  } catch (err) {
    res.status(500).send(err.message);
  }
};