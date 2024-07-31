package com.uphar.datasource.networkmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TransactionResponse(
    @SerializedName("desc")
    @Expose
    val desc: String,

    @SerializedName("amount")
    @Expose
    val amount: Int,

    @SerializedName("recieverAccNo")
    @Expose
    val receiverAccNo: Int,

    @SerializedName("senderAccNo")
    @Expose
    val senderAccNo: Int,

    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("status")
    @Expose
    val status: String,

    @SerializedName("_id")
    @Expose
    val id: String,

    @SerializedName("created_at")
    @Expose
    val createdAt: String,

    @SerializedName("__v")
    @Expose
    val version: Int
)
