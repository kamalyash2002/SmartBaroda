const Transaction = require('../models/Transaction');
const User = require('../models/User');

exports.createTransaction = async (req, res) => {
  try {
    const { desc, amount, recieverAccNo, status, type } = req.body;

    const senderAccNo = req.user.accNo;

    const sender = await User.findOne({ accNo: senderAccNo });
    const reciever = await User.findOne({ accNo: recieverAccNo });

    if (!sender || !reciever) {
      return res.status(404).json({ msg: 'Sender or Receiver not found' });
    }

    if (sender.balance < amount) {
      return res.status(400).json({ msg: 'Insufficient balance' });
    }

    // Update the sender's and receiver's balances
    sender.balance -= amount;
    reciever.balance += amount;

    await sender.save();
    await reciever.save();

    const transaction = new Transaction({
      desc,
      amount,
      recieverAccNo,
      senderAccNo,
      status,
      type
    });

    await transaction.save();

    res.status(201).json(transaction);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

exports.getAllTransactions = async (req, res) => {
  try {
    const transactions = await Transaction.find({
      $or: [{ senderAccNo: req.user.accNo }, { recieverAccNo: req.user.accNo }]
    });
    res.status(200).json(transactions);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

exports.getTransactionById = async (req, res) => {
  try {
    const transaction = await Transaction.findById(req.params.id);
    if (!transaction) {
      return res.status(404).json({ msg: 'Transaction not found' });
    }
    if (transaction.senderAccNo !== req.user.accNo && transaction.recieverAccNo !== req.user.accNo) {
      return res.status(403).json({ msg: 'User not authorized to view this transaction' });
    }
    res.status(200).json(transaction);
    if (!transaction) {
      return res.status(404).json({ msg: 'Transaction not found' });
    }
    if (transaction.senderAccNo !== req.user.accNo && transaction.recieverAccNo !== req.user.accNo) {
      return res.status(403).json({ msg: 'User not authorized to view this transaction' });
    }
    res.status(200).json(transaction);
  } catch (err) {
    res.status(500).json({ error: err.message });
    res.status(500).json({ error: err.message });
  }
};
