package com.example.infuxion_alternate.utils

import android.content.Context
import android.provider.Settings

object NetworkUtil {
    private val TAG = NetworkUtil::class.java.simpleName

    fun isInFlightMode(context: Context): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        ) != 0
    }
}