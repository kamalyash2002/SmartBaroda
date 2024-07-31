const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const TransactionSchema = new Schema({
    desc: { type: String },
    amount: { type: Number, required: true },
    recieverAccNo: { type: Number, required: true },
    senderAccNo: { type: Number, required: true },
    type: { type: String, enum: ['Food', 'Groceries', 'Travel', 'Bills', 'Loan', 'Anonymous', 'Salary'], required: true, default: 'Anonymous' },
    status: { type: String, required: true },
    created_at: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Transaction', TransactionSchema);
