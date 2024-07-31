package com.uphar.datasource.networkmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PolicyRecommendationNetworkResponse(
    @SerializedName("policyType")
    @Expose
    val policyType: String,

    @SerializedName("policyDescription")
    @Expose
    val policyDescription: String,

    @SerializedName("premiumAmount")
    @Expose
    val premiumAmount: String,

    @SerializedName("policyTenure")
    @Expose
    val policyTenure: String,

    @SerializedName("maturityPeriod")
    @Expose
    val maturityPeriod: String,

    @SerializedName("endAmount")
    @Expose
    val endAmount: String,

    @SerializedName("insuranceCover")
    @Expose
    val insuranceCover: Boolean, // Change to Boolean for proper type mapping

    @SerializedName("insuranceAmount")
    @Expose
    val insuranceAmount: String,

    @SerializedName("benefits")
    @Expose
    val benefits: String,

    @SerializedName("downloadableDocument")
    @Expose
    val downloadableDocument: String
)