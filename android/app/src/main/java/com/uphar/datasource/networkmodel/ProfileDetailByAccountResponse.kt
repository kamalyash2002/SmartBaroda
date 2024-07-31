package com.uphar.datasource.networkmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileDetailByAccountResponse(
    @SerializedName("user")
    @Expose
    val user: UserResponse?,

    @SerializedName("sellerDetails")
    @Expose
    val sellerDetails: SellerDetailsResponse?
)

data class UserResponse(
    @SerializedName("_id")
    @Expose
    val id: String?,

    @SerializedName("accNo")
    @Expose
    val accountNumber: Int?,

    @SerializedName("username")
    @Expose
    val username: String?,

    @SerializedName("panCard")
    @Expose
    val panCard: String?,

    @SerializedName("cibilScore")
    @Expose
    val cibilScore: Int?,

    @SerializedName("type")
    @Expose
    val type: String?,

    @SerializedName("GSTIN")
    @Expose
    val gstin: String?,

    @SerializedName("password")
    @Expose
    val password: String?,

    @SerializedName("balance")
    @Expose
    val balance: Int?,

    @SerializedName("created_at")
    @Expose
    val createdAt: String?,

    @SerializedName("__v")
    @Expose
    val v: Int?
)

data class SellerDetailsResponse(
    @SerializedName("_id")
    @Expose
    val id: String?,

    @SerializedName("gstNo")
    @Expose
    val gstNo: String?,

    @SerializedName("sellerName")
    @Expose
    val sellerName: String?,

    @SerializedName("type")
    @Expose
    val type: String?,

    @SerializedName("desc")
    @Expose
    val desc: String?,

    @SerializedName("__v")
    @Expose
    val v: Int?
)
