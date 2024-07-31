const express = require('express');
const router = express.Router();
const transactionController = require('../controllers/transactionController');
const auth = require('../middleware/auth');

// Create a new transaction
router.post('/', auth, transactionController.createTransaction);

// Get all transactions for logged in user
router.get('/', auth, async (req, res) => {
  try {
    const transactions = await Transaction.find({ $or: [{ sender: req.user.id }, { receiver: req.user.id }] });
    res.status(200).send(transactions);
  } catch (err) {
    res.status(400).send(err);
  }
});

// Get transaction by ID
router.get('/:id', auth, transactionController.getTransactionById);

module.exports = router;
