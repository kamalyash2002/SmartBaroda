package com.example.infuxion_alternate.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.infuxion_alternate.LoadingDialog
import com.example.infuxion_alternate.utils.BetterActivityResult
import com.example.infuxion_alternate.utils.LocationFetchUtil
import com.example.infuxion_alternate.utils.TinyDB

abstract class BaseActivity : AppCompatActivity(), View.OnClickListener{

    var activity: Activity?=null
    var tinyDB: TinyDB?=null
    var loader:LoadingDialog?=null
    var locationFetchUtil : LocationFetchUtil?=null
    val activityLauncher = BetterActivityResult.registerActivityForResult(this)

    protected abstract fun init()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        tinyDB = TinyDB(this)
        loader = LoadingDialog(this)

        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        locationFetchUtil?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationFetchUtil?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}