package com.uphar.datasource.networkmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("accNo")
    @Expose
    val accNo: Int,
    @SerializedName("password")
    @Expose
    val password: String
)