const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const LoansIssuedSchema = new Schema({
  desc: { type: String },
  recieverAccNo: { type: Number, required: true },
  type: { type: String, required: true },
  amount: { type: Number, required: true },
  paid: { type: Number, default: 0 },
  loanId: { type: Schema.Types.ObjectId, ref: 'LoanDetails', required: true },
  interest: { type: Number, required: true },
  status: { type: String, required: true },
  created_at: { type: Date, default: Date.now }
});

module.exports = mongoose.model('LoansIssued', LoansIssuedSchema);
