package com.jhalakXCorp.vent.bussinesss.domain.Utils.state

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

class ErrorUtils constructor(
    private val retrofit: Retrofit
) {

    fun parseError(response: Response<*>): APIError? {
        val converter: Converter<ResponseBody, APIError> = retrofit
            .responseBodyConverter(APIError::class.java, arrayOfNulls<Annotation>(0))
        val error: APIError = try {
            response.errorBody()?.let { converter.convert(it) }!!
        } catch (e: IOException) {
            return APIError()
        }
        return error
    }
}