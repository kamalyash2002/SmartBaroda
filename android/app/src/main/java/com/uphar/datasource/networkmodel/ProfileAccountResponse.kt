package com.uphar.datasource.networkmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileAccountResponse(
    @SerializedName("_id")
    @Expose
    val id: String,

    @SerializedName("accNo")
    @Expose
    val accountNumber: Int,

    @SerializedName("username")
    @Expose
    val username: String,

    @SerializedName("panCard")
    @Expose
    val panCard: String,

    @SerializedName("cibilScore")
    @Expose
    val cibilScore: Int,

    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("password")
    @Expose
    val password: String,

    @SerializedName("balance")
    @Expose
    val balance: Int,

    @SerializedName("created_at")
    @Expose
    val createdAt: String,

    @SerializedName("__v")
    @Expose
    val version: Int,

    @SerializedName("transactions")
    @Expose
    val transactions: List<Transaction>,

    @SerializedName("loans")
    @Expose
    val loans: List<Loan>
)

data class Transaction(
    @SerializedName("_id")
    @Expose
    val id: String,

    @SerializedName("desc")
    @Expose
    val description: String,

    @SerializedName("amount")
    @Expose
    val amount: Int,

    @SerializedName("recieverAccNo")
    @Expose
    val receiverAccountNumber: Int,

    @SerializedName("senderAccNo")
    @Expose
    val senderAccountNumber: Int,

    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("status")
    @Expose
    val status: String,

    @SerializedName("created_at")
    @Expose
    val createdAt: String,

    @SerializedName("__v")
    @Expose
    val version: Int,

    @SerializedName("isSender")
    @Expose
    val isSender: Boolean
)

data class Loan(
    @SerializedName("_id")
    @Expose
    val id: String,

    @SerializedName("desc")
    @Expose
    val description: String,

    @SerializedName("recieverAccNo")
    @Expose
    val receiverAccountNumber: Int,

    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("amount")
    @Expose
    val amount: Int,

    @SerializedName("paid")
    @Expose
    val paid: Int,

    @SerializedName("loanId")
    @Expose
    val loanId: String,

    @SerializedName("interest")
    @Expose
    val interest: Int,

    @SerializedName("status")
    @Expose
    val status: String,

    @SerializedName("created_at")
    @Expose
    val createdAt: String,

    @SerializedName("__v")
    @Expose
    val version: Int
)
