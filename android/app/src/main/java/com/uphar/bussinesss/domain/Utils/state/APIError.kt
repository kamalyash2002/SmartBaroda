package com.jhalakXCorp.vent.bussinesss.domain.Utils.state

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Keep
data class APIError(

    @SerializedName("statusCode")
    @Expose
    val statusCode: Int,
    @SerializedName("message")
    @Expose
    val message: List<String>,
    @SerializedName("error")
    @Expose
    val error: String
) {
    constructor() : this(400, listOf("Something went wrong"), "Bad Request")
}