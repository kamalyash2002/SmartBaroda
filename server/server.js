// import dotenv from 'dotenv';
const dotenv = require('dotenv');
dotenv.config();
// import express from 'express';
const express = require('express');
// import authRouter from './routers/authRoutes.js';
// import { authenticateToken } from './middlewares/authToken.js';
// import connectDB from './utils/db.js';
const connectDB = require('./utils/db.js');

const userRoutes = require('./routes/user.js');
const gstRoutes = require('./routes/gst.js');
const transactionRoutes = require('./routes/transactions.js');
const loanIssuedRoutes = require('./routes/loanIssued.js');
const loanDetailRoutes = require('./routes/loanDetails.js');
const policyDetailRoutes = require('./routes/policyDetails.js');
const policySubscribedRoutes = require('./routes/policySubscribed.js');
const authRoutes = require('./routes/auth.js');

const app = express();

app.use(express.json());

connectDB();

app.get('/', (req, res) => {
    res.send('Welcome to the SmartBOB API!');
});

// app.use('/auth', authRouter);

// app.get('/protected', authenticateToken, (req, res) => {
//     res.send('Protected route, only accessible with a valid JWT token');
// });

app.use('/user', userRoutes);
app.use('/gst', gstRoutes);
app.use('/transactions', transactionRoutes);
app.use('/loans-issued', loanIssuedRoutes);
app.use('/loans', loanDetailRoutes);
app.use('/policy-details', policyDetailRoutes);
app.use('/policies-subscribed', policySubscribedRoutes);
app.use('/auth', authRoutes);

app.listen(process.env.PORT || 3000, () => {
    console.log(`Server is running on port ${process.env.PORT || 3000}`);
});