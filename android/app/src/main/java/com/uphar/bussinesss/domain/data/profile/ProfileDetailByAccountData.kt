package com.uphar.bussinesss.domain.data.profile

data class ProfileDetailByAccountData(
    val user: User,
    val sellerDetails: SellerDetails
)

data class User(
    val id: String,
    val accountNumber: Int,
    val username: String,
    val panCard: String,
    val cibilScore: Int,
    val type: String,
    val gstin: String,
    val password: String,
    val balance: Int,
    val createdAt: String,
    val v: Int
)

data class SellerDetails(
    val id: String,
    val gstNo: String,
    val sellerName: String,
    val type: String,
    val desc: String,
    val v: Int
)
