package com.uphar.datasource.networkmodel


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FinanceChatBotNetworkResponse(
    @SerializedName("answer")
    @Expose
    val answer: String
)

data class SmartNotificationsNetworkResponse(
    @SerializedName("smartNotifications")
    @Expose
    val answer: String
)
