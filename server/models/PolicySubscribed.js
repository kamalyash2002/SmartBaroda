const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const PoliciesSubscribedSchema = new Schema({
  desc: { type: String },
  recieverAccNo: { type: Number, required: true },
  type: { type: String, required: true },
  premium: { type: Number, required: true },
  policyId: { type: Schema.Types.ObjectId, ref: 'PolicyDetails', required: true },
  created_at: { type: Date, default: Date.now }
});

module.exports = mongoose.model('PoliciesSubscribed', PoliciesSubscribedSchema);
