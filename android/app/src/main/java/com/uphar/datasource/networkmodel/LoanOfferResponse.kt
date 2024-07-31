package com.uphar.datasource.networkmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoanOfferResponse(
    @SerializedName("_id")
    @Expose
    val id: String,

    @SerializedName("desc")
    @Expose
    val desc: String,

    @SerializedName("interest")
    @Expose
    val interest: Interest,

    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("requiredCibil")
    @Expose
    val requiredCibil: RequiredCibil,

    @SerializedName("benifits")
    @Expose
    val benefits: String,

    @SerializedName("downloadableDoc")
    @Expose
    val downloadableDoc: String,

    @SerializedName("created_at")
    @Expose
    val createdAt: String,

    @SerializedName("__v")
    @Expose
    val version: Int
)

data class Interest(
    @SerializedName("\$numberDecimal")
    @Expose
    val numberDecimal: String
)

data class RequiredCibil(
    @SerializedName("\$numberDecimal")
    @Expose
    val numberDecimal: String
)
