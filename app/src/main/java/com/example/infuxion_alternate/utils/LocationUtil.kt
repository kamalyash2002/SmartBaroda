package com.example.infuxion_alternate.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.*
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.infuxion_alternate.utils.AppConstant.DefaultValue.DEFAULT_LATITUDE
import com.example.infuxion_alternate.utils.AppConstant.DefaultValue.DEFAULT_LONGITUDE
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.Task
import java.lang.ref.WeakReference
import java.util.*

private var TAG = "LocationUtil"

data class LatLng(var latitude: Double, var longitude: Double)

@SuppressLint("MissingPermission")
class LocationFetchUtil(
    private val activity: Activity,
    private val fragment: Fragment? = null,
    private val shouldRequestPermissions: Boolean=false,
    private val shouldRequestOptimization: Boolean=false,
    private val callbacks: Callbacks
) {
    private val nwUtil:NetworkUtil = NetworkUtil
    private var activityWeakReference = WeakReference(activity)
    private var locationCallback: LocationCallback? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val requestCheckSettings = 1111
    private val requestLocation = 1112
    val defaultLocation:Location= Location(LocationManager.NETWORK_PROVIDER).apply {
        this.latitude = DEFAULT_LATITUDE
        this.longitude = DEFAULT_LONGITUDE
    }

    interface Callbacks {
        fun onSuccess(location: Location)
        fun onFailed(locationFailedEnum: LocationFailedEnum,defaultLocation:Location)
    }

    enum class LocationFailedEnum {
        DeviceInFlightMode,
        LocationPermissionNotGranted,
        LocationOptimizationPermissionNotGranted,
        HighPrecisionNA_TryAgainPreferablyWithInternet
    }

    init {
        //try to get last available location, if it matches our precision level
        fusedLocationClient = activity.let { LocationServices.getFusedLocationProviderClient(it) }
        val task = fusedLocationClient?.lastLocation

        task?.addOnSuccessListener { location: Location? ->
            if (location != null) {
                callbacks.onSuccess(location)
            } else {
                onLastLocationFailed()
            }
        }
        task?.addOnFailureListener {
            onLastLocationFailed()
        }
    }

    private fun onLastLocationFailed() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { callbacks.onSuccess(it) }
                locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
            }

            @SuppressLint("MissingPermission")
            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                super.onLocationAvailability(locationAvailability)
                if (!locationAvailability.isLocationAvailable) {
                    callbacks.onFailed(LocationFailedEnum.HighPrecisionNA_TryAgainPreferablyWithInternet,defaultLocation)
                    locationCallback?.let { fusedLocationClient?.removeLocationUpdates(it) }
                }
            }
        }

        if (activityWeakReference.get() == null)
            return
        if (nwUtil.isInFlightMode(activity))
            callbacks.onFailed(LocationFailedEnum.DeviceInFlightMode,defaultLocation)
        else {
            val permissions = ArrayList<String>()
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            var permissionGranted = true
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        activityWeakReference.get() as Activity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionGranted = false
                    break
                }
            }
            if (!permissionGranted) {
                if (shouldRequestPermissions) {
                    val permissionsArgs = permissions.toTypedArray()
                    if (fragment != null)
                        fragment.requestPermissions(
                            permissionsArgs,
                            requestLocation
                        )
                    else
                        ActivityCompat.requestPermissions(
                            activityWeakReference.get() as Activity,
                            permissionsArgs,
                            requestLocation
                        )
                } else {
                    callbacks.onFailed(LocationFailedEnum.LocationPermissionNotGranted,defaultLocation)
                }
            } else {
                getLocation()
            }
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (activityWeakReference.get() == null) {
            return
        }

        if (requestCode == requestLocation) {
            if (grantResults.isEmpty()) {
                callbacks.onFailed(LocationFailedEnum.LocationPermissionNotGranted,defaultLocation)
                return
            }

            var granted = true
            for (grantResult in grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    granted = false
                    break
                }
            }
            if (granted) {
                getLocation()
            } else {
                callbacks.onFailed(LocationFailedEnum.LocationPermissionNotGranted,defaultLocation)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {

        if (activityWeakReference.get() == null) {
            return
        }

        val locationRequest: LocationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,10000).setMaxUpdates(1).build()

        val task: Task<LocationSettingsResponse> =
            (LocationServices.getSettingsClient(activityWeakReference.get() as Activity))
                .checkLocationSettings(
                    (LocationSettingsRequest.Builder().addLocationRequest(
                        locationRequest
                    )).build()
                )

        task.addOnSuccessListener {
            locationCallback?.let {
                fusedLocationClient?.requestLocationUpdates(
                    locationRequest,
                    it,
                    Looper.myLooper()
                )
            }
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                if (activityWeakReference.get() == null) {
                    return@addOnFailureListener
                }
                try {
                    if (shouldRequestOptimization) {
                        if (fragment != null)
                            activity.startIntentSenderForResult(
                                exception.resolution.intentSender,
                                requestCheckSettings,
                                null,
                                0,
                                0,
                                0,
                                null
                            )
                        else
                            exception.startResolutionForResult(
                                activityWeakReference.get() as Activity,
                                requestCheckSettings
                            )
                    } else {
                        callbacks.onFailed(LocationFailedEnum.LocationOptimizationPermissionNotGranted,defaultLocation)
                    }
                } catch (sendEx: IntentSender.SendIntentException) {
                    //ignore the error
                }
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activityWeakReference.get() == null) {
            return
        }

        if (requestCode == requestCheckSettings) {
            if (resultCode == Activity.RESULT_OK) {
                getLocation()
            } else {
                val locationManager =
                    (activityWeakReference.get() as Activity).getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    callbacks.onFailed(LocationFailedEnum.HighPrecisionNA_TryAgainPreferablyWithInternet,defaultLocation)
                } else {
                    "$TAG, LocationOptimizationPermissionNotGranted, GPS_PROVIDER:-Disable".logD()
                    val task = fusedLocationClient?.lastLocation

                    task?.addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            "$TAG, addOnSuccessListener, GPS_PROVIDER disable but get current location".logD()
                            callbacks.onSuccess(location)
                        } else {
                            "$TAG, addOnSuccessListener, GPS_PROVIDER disable fail to  get current location".logD()
                            callbacks.onFailed(LocationFailedEnum.LocationOptimizationPermissionNotGranted,defaultLocation)
                        }
                    }
                    task?.addOnFailureListener {
                        "$TAG, addOnFailureListener, GPS_PROVIDER disable fail to  get current location".logD()
                        callbacks.onFailed(LocationFailedEnum.LocationOptimizationPermissionNotGranted,defaultLocation)
                    }
                }
            }
        }
    }
}

