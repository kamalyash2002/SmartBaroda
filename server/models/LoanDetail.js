const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const LoanDetailSchema = new Schema({
  desc: { type: String, required: true },
  interest: { type: mongoose.Types.Decimal128, required: true },
  type: { type: String, required: true },
  requiredCibil: { type: mongoose.Types.Decimal128, required: true },
  benifits: { type: String, required: true },
  downloadableDoc: { type: String, required: true },
  created_at: { type: Date, default: Date.now }
});

module.exports = mongoose.model('LoanDetail', LoanDetailSchema);
