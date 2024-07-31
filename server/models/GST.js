const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const GSTSchema = new Schema({
  gstNo: { type: String, required: true, unique: true },
  sellerName: { type: String, required: true },
  type: { type: String, required: true },
  desc: { type: String, required: true }
});

module.exports = mongoose.model('GST', GSTSchema);
