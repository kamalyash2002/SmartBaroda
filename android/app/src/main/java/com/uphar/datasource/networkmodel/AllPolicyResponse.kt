package com.uphar.datasource.networkmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.Date

data class AllPolicyItemResponse(
    @SerializedName("_id")
    @Expose
    val id: String,

    @SerializedName("desc")
    @Expose
    val description: String,

    @SerializedName("premium")
    @Expose
    val premium: DecimalValue,

    @SerializedName("tenure")
    @Expose
    val tenure: DecimalValue,

    @SerializedName("maturity")
    @Expose
    val maturity: DecimalValue,

    @SerializedName("endAmount")
    @Expose
    val endAmount: DecimalValue,

    @SerializedName("insuranceCover")
    @Expose
    val insuranceCover: Boolean,

    @SerializedName("insuranceAmount")
    @Expose
    val insuranceAmount: DecimalValue,

    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("benifits")
    @Expose
    val benefits: String,

    @SerializedName("downloadableDoc")
    @Expose
    val downloadableDocument: String,

    @SerializedName("created_at")
    @Expose
    val createdAt: String,

    @SerializedName("__v")
    @Expose
    val version: Int
)

data class DecimalValue(
    @SerializedName("\$numberDecimal")
    @Expose
    val value: String
)