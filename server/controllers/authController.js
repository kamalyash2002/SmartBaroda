const jwt = require('jsonwebtoken');
const { validationResult } = require('express-validator');
const User = require('../models/User');

exports.register = async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }

  try {
    const { accNo, username, panCard, cibilScore, type, GSTIN, password, balance } = req.body;

    let user = await User.findOne({ accNo });
    if (user) {
      return res.status(400).json({ msg: 'User already exists' });
    }

    user = new User({ accNo, username, panCard, cibilScore, type, GSTIN, password, balance });
    await user.save();

    const payload = { user: { id: user.id, accNo: user.accNo } };
    jwt.sign(payload, 'secret', { expiresIn: '10d' }, (err, token) => {
      if (err) throw err;
      res.json({ token });
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).send('Server error');
  }
};

exports.login = async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ errors: errors.array() });
  }

  const { accNo, password } = req.body;

  try {
    let user = await User.findOne({ accNo });
    if (!user) {
      return res.status(400).json({ msg: 'Invalid Credentials' });
    }

    const isMatch = await user.comparePassword(password);
    if (!isMatch) {
      return res.status(400).json({ msg: 'Invalid Credentials' });
    }

    const payload = { user: { id: user.id, accNo: user.accNo } };
    jwt.sign(payload, 'secret', { expiresIn: '10d' }, (err, token) => {
      if (err) throw err;
      res.json({ token });
    });
  } catch (err) {
    console.error(err.message);
    res.status(500).send('Server error');
  }
};
