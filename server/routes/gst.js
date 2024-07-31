const express = require('express');
const router = express.Router();
const gstController = require('../controllers/gstController');

router.post('/', gstController.createGST);

router.get('/', gstController.getAllGST);

router.get('/:id', gstController.getGSTById);

module.exports = router;
