package com.example.infuxion_alternate.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.biometric.BiometricManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.infuxion_alternate.LoadingDialog
import com.example.infuxion_alternate.model.ContactModel
import com.example.infuxion_alternate.model.SmsModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.*
import java.io.File
import java.io.IOException
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow


object MethodMasterOld {

    private const val TAG = "MethodMaster"

    fun View.show() {
        visibility = View.VISIBLE
    }

    fun View.hide() {
        visibility = View.GONE
    }

    fun biometricIsWorkOrNot(
        context: Context?,
        callbacks: ((isWorking: Boolean, hasFingerprintLockAvailable: Boolean, message: String) -> Unit)
    ) {
        context?.let { activity ->
            val biometricManager: BiometricManager = BiometricManager.from(activity)
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    "Biometric, Biometric has Available".logD()
                    callbacks.invoke(true, true, "Biometric has Available")
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    "Biometric, Biometric Authentication currently unavailable".logD()
                    callbacks.invoke(false, false, "Biometric Authentication currently unavailable")
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    "Biometric, Your device doesn't support Biometric Authentication".logD()
                    callbacks.invoke(
                        false,
                        false,
                        "Your device doesn't support Biometric Authentication"
                    )
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    "Biometric, Your device doesn't have any fingerprint enrolled".logD()
                    callbacks.invoke(
                        true,
                        false,
                        "Your device doesn't have any fingerprint enrolled"
                    )
                }
                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                    "Biometric, Your device have security update required".logD()
                    callbacks.invoke(false, false, "Your device have security update required")
                }
                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                    "Biometric, Biometric unsupported".logD()
                    callbacks.invoke(false, false, "Biometric unsupported")
                }
                BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                    "Biometric, Biometric status unknown".logD()
                    callbacks.invoke(false, false, "Biometric status unknown")
                }
            }
        }
    }

    fun checkLockIsEnable(tinyDB: TinyDB?): Boolean {
        return tinyDB?.getBoolean(AppConstant.Prefs.SHOW_PIN_LOCK) == true
    }

    fun showToast(context: Context? = null, msg: String?=null, dur: Int = Toast.LENGTH_SHORT) {
        context?.takeIf { msg?.isNotEmpty() == true }?.apply {
            Toast.makeText(this, msg, dur).show()
        }
    }

    suspend fun getAllSmsMessages(context: Context?): List<SmsModel> = withContext(Dispatchers.IO) {
        val smsList = mutableListOf<SmsModel>()
        val contentResolver = context?.contentResolver
        val uri = Uri.parse("content://sms/inbox")
        val cursor = contentResolver?.query(uri, null, null, null, null)

        cursor?.apply {
            if (moveToFirst()) {
                do {
                    val id = getString(getColumnIndexOrThrow("_id"))
                    val threadId = getString(getColumnIndexOrThrow("thread_id"))
                    val phoneNumber = getString(getColumnIndexOrThrow("address"))
                    val body = getString(getColumnIndexOrThrow("body"))
                    val readState =getString(getColumnIndexOrThrow("read"))
                    val timestamp = getString(getColumnIndexOrThrow("date"))
                    val type = if (getString(getColumnIndexOrThrow("type")).contains("1")) "inbox" else "sent"

                    smsList.add(SmsModel(/*id,threadId,phoneNumber,*/ body/*,readState, timestamp,type*/))
                } while (moveToNext())

                close()
            }
        }

        return@withContext filterFinancialMessages(smsList)
    }

    private fun filterFinancialMessages(smsList: List<SmsModel>): List<SmsModel> {
//        val financialKeywords = listOf("balance", "transaction", "credit card", "debit card", "ATM", "bank", "interest", "loan")
        val financialKeywords = listOf("shopping", "airtel", "recharge", "disney", "xstream")
        return smsList.filter { sms -> financialKeywords.any { keyword -> sms.body.contains(keyword, ignoreCase = true) } }
    }

    suspend fun getAllInstalledApps(context: Context): List<Pair<String, String>> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfoList = packageManager.queryIntentActivities(intent, 0)
        val appList = mutableListOf<Pair<String, String>>()

        for (resolveInfo in resolveInfoList) {
            val appName = resolveInfo.loadLabel(packageManager).toString()
            val packageName = resolveInfo.activityInfo.packageName
            appList.add(Pair(appName, packageName))
        }

        return@withContext filterFinancialApp(appList)
    }

    private fun filterFinancialApp(appList: List<Pair<String, String>>):List<Pair<String, String>>{
        val financialPackage = listOf("com.google.android.youtube", "com.android.chrome", "com.spotify.music", "com.facebook.katana","com.instagram.android", "com.google.android.apps.nbu.paisa.user")
        return appList.filter { app -> financialPackage.any { keyword -> app.second.contains(keyword, ignoreCase = true) } }
    }

    @SuppressLint("Range")
    suspend fun fetchAllContacts(context: Context): List<ContactModel> = withContext(Dispatchers.IO) {
        val contactModels = mutableListOf<ContactModel>()
        val contentResolver = context.contentResolver
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val selection = null
        val selectionArgs = null
        val sortOrder = "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} ASC"

        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor = contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use {
            val seenPhoneNumbers = mutableSetOf<String>()

            while (it.moveToNext()) {
                if (contactModels.size<=7) {
                    val phoneNumber = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ", "").replace("-", "")
                    if (!seenPhoneNumbers.contains(phoneNumber)) {
                        val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                        val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))
                        contactModels.add(ContactModel(/*id,*/ name, phoneNumber))
                        seenPhoneNumbers.add(phoneNumber)
                    }
                }
            }
        }

        return@withContext contactModels
    }


    @SuppressLint("HardwareIds")
    fun getImeiNumber(context: Context?){
        context?.apply {
            val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager?
            val imei = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                telephonyManager?.imei
            } else {
                telephonyManager?.deviceId
            }
        }
    }

    val genderOption = listOf("Select Gender", "Male", "Female", "Transgender")

    /*
    * lifecycleScope.launch {
                    compressFile(context,File(data!!.data),true){
                        imageView.setImageURI(Uri.fromFile(it))
                    }
                }
    * */
    suspend fun compressFile(context: Context?, file: File, showLoader: Boolean = true, onCompress: (File) -> Unit) {
        var loader: LoadingDialog?
        showLoader.takeIf { it }.let {
            loader = LoadingDialog(context as Activity)
            loader?.startLoading()
        }

        try {
            "$TAG Original File:- ${file.path}".logD()
            val widthHeight = getImageWidthOrHeight(Uri.parse(file.toString()))
            "$TAG Original File, Width:-${widthHeight.first}, Height:-${widthHeight.second}".logD()
            val outputPath: File? = FileUtil.getDestinationCompressImageFilePath(context, file)

            Compressor.compress(context!!, file) {
                resolution(
                    if (widthHeight.first != 0) widthHeight.first else 1280,
                    if (widthHeight.second != 0) widthHeight.second else 720
                )
                outputPath?.let {
                    destination(it)
                }
            }.also {
                "$TAG Compress File:- ${it.path}".logD()
                "$TAG Compress File, Size:-${it.length()}, FormatSize:-${it.length().formatSize()}, Image Quality".logD()
                onCompressImage(it,loader, onCompress)
            }

        } catch (e: IOException) {
            "$TAG IOException, onCompress File ${e.message}".logD()
            onCompressImage(file, loader, onCompress)
        }catch (e: OutOfMemoryError) {
            "$TAG OutOfMemoryError, onCompress File ${e.message}".logD()
            onCompressImage(file, loader, onCompress)
        } catch (e: Exception) {
            "$TAG Exception, onCompress File ${e.message}".logD()
            onCompressImage(file, loader, onCompress)
        }
    }

    private fun onCompressImage(cropFile: File, loader: LoadingDialog?, onCompress: (File) -> Unit) {
        loader?.isDismiss()
        onCompress.invoke(cropFile)
    }

    private fun getImageWidthOrHeight(uri: Uri): Pair<Int, Int> {
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(uri.path?.let { File(it).absolutePath }, options)
        val imageHeight: Int = options.outHeight
        val imageWidth: Int = options.outWidth
        return Pair(imageWidth, imageHeight)
    }

    private fun log(fileLength: Long, quality: Int): Int {
        "$TAG Original File, Size:-${fileLength}, FormatSize:-${fileLength.formatSize()}, Image Quality, $quality".logD()
        return quality
    }

    private fun Long.formatSize(): String {
        if (this <= 0) return "0"
        val units = arrayOf("Bytes", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(this / 1024.0.pow(digitGroups.toDouble()))
            .toString() + " " + units[digitGroups]
    }

    fun Context.loadImage(
        image: Any?,
        imageView: ImageView,
        requestOptions: RequestOptions? = null,
    ) {
        requestOptions?.let {
            Glide.with(this)
                .applyDefaultRequestOptions(it)
                .load(image)
                .into(imageView)
        }?: kotlin.run {
            Glide.with(this)
                .load(image)
                .into(imageView)
        }
    }
}