package com.uphar.di


import android.app.Application
import com.uphar.bussinesss.domain.Utils.BasePreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application()
{
    private lateinit var basePreferences: BasePreferences


    override fun onCreate() {
        super.onCreate()
        basePreferences = BasePreferences(applicationContext)
    }

}