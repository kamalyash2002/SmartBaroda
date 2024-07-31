package com.uphar.bussinesss.domain.data.profile

data class AddTransaction(
    val receiverAccNo: Long,
    val desc: String,
    val amount: Double,
    val status: String,
    val type: String
)
