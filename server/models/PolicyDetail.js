const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const PolicyDetailSchema = new Schema({
  desc: { type: String, required: true },
  premium: { type: mongoose.Types.Decimal128, required: true },
  tenure: { type: mongoose.Types.Decimal128, required: true },
  maturity: { type: mongoose.Types.Decimal128, required: true },
  endAmount: { type: mongoose.Types.Decimal128, required: true },
  insuranceCover: { type: Boolean, required: true },
  insuranceAmount: { type: mongoose.Types.Decimal128 },
  type: { type: String, required: true },
  benifits: { type: String, required: true },
  downloadableDoc: { type: String, required: true },
  created_at: { type: Date, default: Date.now }
});

module.exports = mongoose.model('PolicyDetail', PolicyDetailSchema);
