package com.uphar.datasource.networkmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoanRecommendationRequest(
    @SerializedName("userPrompt")
    @Expose
    val userPrompt: String
)
data class LoanRecommendationResponse(
    @SerializedName("loanType")
    @Expose
    val loanType: String,

    @SerializedName("loanAmount")
    @Expose
    val loanAmount: String,

    @SerializedName("loanTenure")
    @Expose
    val loanTenure: String,

    @SerializedName("interestRate")
    @Expose
    val interestRate: String,

    @SerializedName("emi")
    @Expose
    val emi: String,

    @SerializedName("processingFee")
    @Expose
    val processingFee: String,

    @SerializedName("totalCost")
    @Expose
    val totalCost: String
)