const GST = require('../models/GST');

// Create a new GST
exports.createGST = async (req, res) => {
  try {
    const gst = new GST(req.body);
    await gst.save();
    res.status(201).send(gst);
  } catch (err) {
    res.status(400).send(err);
  }
};

// Get all GST records
exports.getAllGST = async (req, res) => {
  try {
    const gstRecords = await GST.find();
    res.status(200).send(gstRecords);
  } catch (err) {
    res.status(400).send(err);
  }
};

// Get GST by ID
exports.getGSTById = async (req, res) => {
  try {
    const gst = await GST.findById(req.params.id);
    if (!gst) return res.status(404).send();
    res.status(200).send(gst);
  } catch (err) {
    res.status(400).send(err);
  }
};
