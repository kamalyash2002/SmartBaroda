package com.uphar.bussinesss.domain.data.profile


data class ProfileResponseData(
    val id: String,
    val accountNumber: Int,
    val username: String,
    val panCard: String,
    val cibilScore: Int,
    val type: String,
    val password: String,
    val balance: Int,
    val createdAt: String,
    val version: Int,
    val transactions: List<Transaction>,
    val loans: List<Loan>
)

data class Transaction(
    val id: String,
    val description: String,
    val amount: Int,
    val receiverAccountNumber: Int,
    val senderAccountNumber: Int,
    val type: String,
    val status: String,
    val createdAt: String,
    val version: Int,
    val isSender: Boolean
)

data class Loan(
    val id: String,
    val description: String,
    val receiverAccountNumber: Int,
    val type: String,
    val amount: Int,
    val paid: Int,
    val loanId: String,
    val interest: Int,
    val status: String,
    val createdAt: String,
    val version: Int
)
