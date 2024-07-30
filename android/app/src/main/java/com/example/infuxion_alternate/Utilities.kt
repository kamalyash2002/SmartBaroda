package com.example.infuxion_alternate

import android.util.Log

object Utilities {

    private var TAG = "Neo Saturn"

    fun logEwithoutstore(description : String){
        Log.e(TAG,""+description)
    }

    fun logW(description : String){
        Log.w(TAG,""+description)
    }
    fun logI(description : String){
        Log.i(TAG,""+description)
    }
    fun logD(description : String){
        Log.d(TAG,""+description)
    }
    fun logV(description : String){
        Log.v(TAG,""+description)
    }

}