const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');
const Schema = mongoose.Schema;

const UserSchema = new Schema({
  accNo: { type: Number, required: true, unique: true },
  username: { type: String, required: true },
  panCard: { type: String, required: true },
  cibilScore: { type: Number, required: true },
  type: { type: String, required: true },
  GSTIN: { type: String },
  password: { type: String, required: true },
  balance: { type: Number, default: 0 },
  created_at: { type: Date, default: Date.now },
});

// Password hashing middleware
UserSchema.pre('save', async function(next) {
  if (!this.isModified('password')) return next();
  const salt = await bcrypt.genSalt(10);
  this.password = await bcrypt.hash(this.password, salt);
  next();
});

UserSchema.methods.comparePassword = async function(password) {
  return await bcrypt.compare(password, this.password);
};

module.exports = mongoose.model('User', UserSchema);
