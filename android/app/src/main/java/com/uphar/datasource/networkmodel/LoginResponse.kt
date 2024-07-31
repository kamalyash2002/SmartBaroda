package com.uphar.datasource.networkmodel

import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class LoginResponse(
    @SerializedName("token")
    @Expose
    val token: String?,
    @SerializedName("msg")
    @Expose
    val msg: String?,
)
