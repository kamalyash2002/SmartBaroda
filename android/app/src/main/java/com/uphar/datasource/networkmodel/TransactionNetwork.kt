package com.uphar.datasource.networkmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



data class TransactionNetwork(
    @SerializedName("recieverAccNo")
    @Expose
    val receiverAccNo: Long,

    @SerializedName("desc")
    @Expose
    val desc: String,

    @SerializedName("amount")
    @Expose
    val amount: Double,

    @SerializedName("status")
    @Expose
    val status: String,

    @SerializedName("type")
    @Expose
    val type: String
)