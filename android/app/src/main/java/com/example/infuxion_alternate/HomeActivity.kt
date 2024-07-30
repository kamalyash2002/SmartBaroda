package com.example.infuxion_alternate

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.infuxion_alternate.base.BaseActivity
import com.example.infuxion_alternate.utils.AppConstant.DefaultValue.DEFAULT_LATITUDE
import com.example.infuxion_alternate.utils.AppConstant.DefaultValue.DEFAULT_LONGITUDE
import com.example.infuxion_alternate.utils.LocationFetchUtil
import com.example.infuxion_alternate.utils.MethodMaster.fetchAllContacts
import com.example.infuxion_alternate.utils.MethodMaster.getAllInstalledApps
import com.example.infuxion_alternate.utils.MethodMaster.getAllSmsMessages
import com.example.infuxion_alternate.utils.MethodMaster.getImeiNumber
import com.example.infuxion_alternate.utils.MethodMaster.showToast
import com.example.infuxion_alternate.utils.logD
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity() {

    private val TAG="HomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNav =findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val fragmentContainer =findViewById<View>(R.id.mainFragment)

        bottomNav.setupWithNavController(fragmentContainer.findNavController())

        init()
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    override fun init() {
//        getContact()
//        getFinancialSms()
//        getInstallApp()
//        getImeiNum()
        getLocation()
    }

//    private fun getImeiNum(){
//        activity?.let {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
//                lifecycleScope.launch {
//                    val imei = getImeiNumber(it)
//                    "$TAG IMEI Number:-${imei}".logD()
//                }
//            }
//        }
//    }
//
//    private fun getContact() {
//        activity?.let {
//            if (ContextCompat.checkSelfPermission(it, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED){
//                lifecycleScope.launch {
//                    val contactList = fetchAllContacts(it)
//                    for (contact in contactList) {
//                        "$TAG Contact Model:-${contact}".logD()
//                    }
//                }
//            }
//        }
//    }
//
//    private fun getFinancialSms() {
//        activity?.let {
//            if (ContextCompat.checkSelfPermission(it, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
//                lifecycleScope.launch {
//                    val smsList = getAllSmsMessages(activity)
//                    val financialSmsList = filterFinancialMessages(smsList)
//                    for (sms in financialSmsList) {
//                        "$TAG SMS Model:-${sms}".logD()
//                    }
//                }
//            }
//        }
//    }
//
//    private fun getInstallApp() {
//        activity?.let {
//            if (ContextCompat.checkSelfPermission(it, Manifest.permission.QUERY_ALL_PACKAGES) == PackageManager.PERMISSION_GRANTED) {
//                lifecycleScope.launch {
//                    val installedApps = getAllInstalledApps(it)
//                    val financialAppList = filterFinancialApp(installedApps)
//                    for (app in financialAppList) {
//                        "$TAG AppName:-${app.first}, PackageName:-${app.second}".logD()
//                    }
//                }
//            }
//        }
//    }

    private fun getLocation(){
        activity?.let { activity ->
            locationFetchUtil = LocationFetchUtil(activity, null, shouldRequestPermissions = true, shouldRequestOptimization = true, callbacks = object : LocationFetchUtil.Callbacks {
                    override fun onSuccess(location: Location) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        "$TAG, onSuccess, Latitude:- $latitude".logD()
                        "$TAG, onSuccess, Longitude:- $longitude".logD()

                        if (latitude != DEFAULT_LATITUDE && longitude != DEFAULT_LONGITUDE) {
                            val latLng= "($latitude, $longitude)"
                            showToast(activity,latLng)
                        }
                    }

                    override fun onFailed(locationFailedEnum: LocationFetchUtil.LocationFailedEnum, defaultLocation: Location) {
                        "$TAG, onFailed, enum:- ${locationFailedEnum.name}".logD()
                    }
                })
        }
    }
}