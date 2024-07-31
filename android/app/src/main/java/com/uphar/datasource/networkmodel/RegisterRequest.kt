package com.uphar.datasource.networkmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("accNo")
    @Expose
    val accNo: Int,

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
    val password: String
)
