package com.uphar.bussinesss.domain.Utils

import android.content.Context

val PREFERENCE_NAME = "language_preference"
val FIREBASE_REFRESH_TOKEN = "firebase_refresh_token"
val MULTIPLE_LOGIN = "multiple_login"
val LOG_OUT_REASON = "login_out_reason"

val LOG_IN_STATUS = "login_status"


val APP_TOKEN = "application_token"
val APP_REFRESH_TOKEN = "application_refresh_token"


class BasePreferences(context: Context?) {

    private val preferences = context?.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)




    fun setFirebaseRefreshedToken(token: String) {
        val editor = preferences?.edit()
        editor?.putString(FIREBASE_REFRESH_TOKEN, token)
        editor?.apply()
    }

    fun getFirebaseRefreshedToken() = preferences?.getString(FIREBASE_REFRESH_TOKEN, "")


    fun setTokens(token: String, refreshToken: String) {
        val editor = preferences?.edit()
        editor?.putString(APP_TOKEN, token)
        editor?.putString(APP_REFRESH_TOKEN, refreshToken)
        editor?.apply()
    }

    fun getToken() = preferences?.getString(APP_TOKEN, "")

    fun getRefreshToken() = preferences?.getString(APP_REFRESH_TOKEN, "")

    fun setMultipleLogin(status: Boolean, reason: String) {
        val editor = preferences?.edit()
        editor?.putBoolean(MULTIPLE_LOGIN, status)
        editor?.putString(LOG_OUT_REASON, reason)
        editor?.apply()
    }

    fun isMultipleLogin() = preferences?.getBoolean(MULTIPLE_LOGIN, false)
    fun getLogOutReason() = preferences?.getString(LOG_OUT_REASON, "")

    fun setLoginStatus(status: Boolean) {
        val editor = preferences?.edit()
        editor?.putBoolean(LOG_IN_STATUS, status)
        editor?.apply()
    }

}