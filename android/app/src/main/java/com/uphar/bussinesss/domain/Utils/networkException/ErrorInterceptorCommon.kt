package com.jhalakXCorp.vent.bussinesss.domain.Utils.networkException

import android.content.Context
import android.content.Intent
import com.uphar.bussinesss.domain.dataStore.basePreference.BasePreferencesManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ErrorInterceptorCommon(
    private val context: Context,
    private val basePreferencesManager: BasePreferencesManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request: Request = chain.request()

        val response = chain.proceed(request)

        // inspect status codes of unsuccessful responses
        when (response.code) {
            400 -> {
                runBlocking {
                    basePreferencesManager.logOut()
                    Intent(
                        context,
                        Class.forName("com.jhalakXCorp.vent.ui.screens.homeScreen")
                    ).apply {
                        this.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(this)
                    }
                   // throw IOException("Unauthorized !!")
                }
            }
        }

        return response
    }
}