package com.uphar.bussinesss.domain.data.login

data class RegisterCredentials(
    val accNo: String = "",
    val username: String = "",
    val panCard: String = "",
    val cibilScore: String = "",
    val type: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val gstIN :String="",
)