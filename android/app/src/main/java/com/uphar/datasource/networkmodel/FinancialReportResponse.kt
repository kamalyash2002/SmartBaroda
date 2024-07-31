package com.uphar.datasource.networkmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Expenditure(
    @SerializedName("Anonymous")
    @Expose
    val anonymous: String,

    @SerializedName("Food")
    @Expose
    val food: String,

    @SerializedName("Groceries")
    @Expose
    val groceries: String,

    @SerializedName("Loan")
    @Expose
    val loan: String,

    @SerializedName("Travel")
    @Expose
    val travel: String
)

data class FinancialReportResponse(
    @SerializedName("expenditure")
    @Expose
    val expenditure: Expenditure,

    @SerializedName("savingsSuggestions")
    @Expose
    val savingsSuggestions: String
)
