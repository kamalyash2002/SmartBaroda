package com.example.infuxion_alternate.app_lock

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.hardware.fingerprint.FingerprintManager
import android.os.*
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.infuxion_alternate.R
import com.example.infuxion_alternate.databinding.ActivityPinLockBinding
import com.example.infuxion_alternate.databinding.DialogBiometricScanningBinding
import com.example.infuxion_alternate.intro.SplashActivity
import com.example.infuxion_alternate.utils.AppConstant
import com.example.infuxion_alternate.utils.FingerprintHandler
import com.example.infuxion_alternate.utils.MethodMaster.biometricIsWorkOrNot
import com.example.infuxion_alternate.utils.MethodMaster.hide
import com.example.infuxion_alternate.utils.MethodMaster.show
import com.example.infuxion_alternate.utils.MethodMaster.showToast
import com.example.infuxion_alternate.utils.TinyDB
import com.example.infuxion_alternate.utils.logD
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

open class PinLockActivity : AppCompatActivity() {

    private var binding: ActivityPinLockBinding? = null
    private var activity:Activity?=null
    private var tinyDB: TinyDB?=null
    private var stringBuilderPin: StringBuilder = StringBuilder()
    private var keyStore: KeyStore? = null
    private val keySoreName = "PinLock"
    private var cipher: Cipher? = null
    private var isCreateNewPin = false
    private var isConfirmNewPin = false
    private var pinMessage: String? = null
    private var currentPin: String? = null
    private var isFrom: String? = null
    private var dialogBinding: DialogBiometricScanningBinding? = null
    private var biometricDialog: Dialog? = null
    private val TAG = "PinLockActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinLockBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        activity = this
        tinyDB = TinyDB(this)

        getIntentData()
        init()
    }

    private fun getIntentData() {
        intent?.extras?.let { bundle ->
            bundle.takeIf { it.containsKey(AppConstant.BundleData.PIN_MESSAGE) }?.let {
                pinMessage = it.getString(AppConstant.BundleData.PIN_MESSAGE)
            }
            bundle.takeIf { it.containsKey(AppConstant.BundleData.CREATE_NEW_PIN) }?.let {
                isCreateNewPin = it.getBoolean(AppConstant.BundleData.CREATE_NEW_PIN)
            }
            bundle.takeIf { it.containsKey(AppConstant.BundleData.IS_FROM) }?.let {
                isFrom = it.getString(AppConstant.BundleData.IS_FROM)
            }
        }
    }

    private fun init() {
        binding?.apply {
            activity?.let {

                textviewPinmessage.text = pinMessage

                if (!isCreateNewPin) {
                    doBiometricWork()
                }

                num1.setOnClickListener {
                    setToStringBuilder("1")
                }
                num2.setOnClickListener {
                    setToStringBuilder("2")
                }
                num3.setOnClickListener {
                    setToStringBuilder("3")
                }
                num4.setOnClickListener {
                    setToStringBuilder("4")
                }
                num5.setOnClickListener {
                    setToStringBuilder("5")
                }
                num6.setOnClickListener {
                    setToStringBuilder("6")
                }
                num7.setOnClickListener {
                    setToStringBuilder("7")
                }
                num8.setOnClickListener {
                    setToStringBuilder("8")
                }
                num9.setOnClickListener {
                    setToStringBuilder("9")
                }
                num0.setOnClickListener {
                    setToStringBuilder("0")
                }

                numBackspace.setOnClickListener {
                    if (stringBuilderPin.toString().trim { it <= ' ' }.isNotEmpty()) {
                        stringBuilderPin.setLength(stringBuilderPin.length - 1)
                    }

                    checkCount()
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun showBioDialog() {
        activity?.let { activity ->
            val view = layoutInflater.inflate(R.layout.dialog_biometric_scanning, null)
            dialogBinding = DialogBiometricScanningBinding.bind(view)
            biometricDialog = Dialog(activity)
            dialogBinding?.root?.let {
                biometricDialog?.let { dialog ->
                    dialog.setContentView(it)
                    dialog.setCancelable(false)
                    dialog.setCanceledOnTouchOutside(false)
                }
            }

            dialogBinding?.apply {

                if(tinyDB?.getBoolean(AppConstant.Prefs.SHOW_PIN_LOCK) == false){
                    skipButton.hide()
                }

                makeGrayFingerLogo(dialogLogo)
                val alert = AlertDialog.Builder(activity)
                val fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
                dialogLogo.startAnimation(fadeInAnimation)
                alert.setCancelable(true)

                skipButton.setOnClickListener {
                    biometricDialog?.dismiss()
                }

                biometricDialog?.setOnCancelListener { biometricDialog?.dismiss() }

                biometricDialog?.window?.apply {
                    setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }

                biometricDialog?.show()
            }
        }
    }

    open fun makeGrayFingerLogo(myImageView: ImageView?) {
        activity?.let { activity ->
            myImageView?.apply {
                DrawableCompat.setTint(
                    drawable,
                    ContextCompat.getColor(activity,R.color.color_dadada)
                )
            }
        }
    }

    open fun fingerScanResult(e: String, success: Boolean) {
        if (success) {
            "$TAG, fingerScanResult, e:-$e, success".logD()
            greenSignalFinger()
            goToNextScreen()
        } else {
            "$TAG, fingerScanResult, error , e:-$e, fail".logD()
            redAlertToFinger()
        }
    }

    private fun greenSignalFinger() {
        vibrateSmall(true)
        makeGreenOrRed(dialogBinding?.dialogLogo, true)
    }

    private fun redAlertToFinger() {
        vibrateSmall(false)
        makeGreenOrRed(dialogBinding?.dialogLogo, false)
    }

    @SuppressLint("ResourceAsColor")
    open fun makeGreenOrRed(myImageView: ImageView?, b: Boolean) {
        activity?.let { activity ->
            myImageView?.apply {
                if (b) {
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(activity, android.R.color.holo_green_light))
                } else {
                    DrawableCompat.setTint(drawable, ContextCompat.getColor(activity, android.R.color.holo_red_light))
                }
            }
            dialogBinding?.apply {
                if (b) {
                    txtSetUpYourTouchId.visibility = View.INVISIBLE
                    txtMsgDialogbiometric.visibility = View.INVISIBLE
                    txtTransferText1.text = getString(R.string.authentication_successful)
                } else {
                    txtSetUpYourTouchId.visibility = View.INVISIBLE
                    txtMsgDialogbiometric.visibility = View.INVISIBLE
                    txtTransferText1.text = resources.getString(R.string.authentication_fail)
                }
            }
        }
    }

    fun changeModeToCreate(mode: Boolean) {
        if (mode) {
            binding?.textviewPinmessage?.text = resources.getString(R.string.create_pin)
        } else {
            binding?.textviewPinmessage?.text = resources.getString(R.string.enter_pin)
        }
        isCreateNewPin = mode
        clearPassword()
    }

    private fun enableDisable(b: Boolean) {
        binding?.apply {
            num0.isEnabled = b
            num1.isEnabled = b
            num2.isEnabled = b
            num3.isEnabled = b
            num4.isEnabled = b
            num5.isEnabled = b
            num6.isEnabled = b
            num7.isEnabled = b
            num8.isEnabled = b
            num9.isEnabled = b
            num0.isEnabled = b
        }
    }

    private fun setToStringBuilder(stringBuilder: String?) {
        stringBuilderPin.toString().trim { it <= ' ' }.length
        if (stringBuilderPin.toString().trim { it <= ' ' }.length < 4) {
            stringBuilderPin.append(stringBuilder)
        }
        checkCount()
    }

    private fun checkCount() {
        setDotCount(stringBuilderPin.toString().trim { it <= ' ' }.length)
        if (stringBuilderPin.toString().trim { it <= ' ' }.length == 4) {
            enableDisable(false)

            if (isConfirmNewPin){
                "$TAG, Confirm New Pin".logD()
                if (currentPin == stringBuilderPin.toString().trim { it <= ' ' }) {
                    "$TAG, Successfully Create New Pin:-${stringBuilderPin.toString().trim { it <= ' ' }}".logD()
                    tinyDB?.putString(AppConstant.Prefs.PIN_PASSWORD, stringBuilderPin.toString().trim { it <= ' ' })
                    setResult(Activity.RESULT_OK)
                    finish()
                }else{
                    "$TAG, Confirm Pin Not Match:-${stringBuilderPin.toString().trim { it <= ' ' }}".logD()
                    showToast(activity, resources.getString(R.string.confirm_pin_not_match))
                    vibrateSmall(false)
                    clearPassword()
                }
            }else if (isCreateNewPin) {
                currentPin = stringBuilderPin.toString().trim { it <= ' ' }
                "$TAG, New Pin:-${currentPin}".logD()

                binding?.textviewPinmessage?.text =
                    resources.getString(R.string.confirm_new_pin)

                isConfirmNewPin = true
                clearPassword()

            } else {
                if (tinyDB?.getString(AppConstant.Prefs.PIN_PASSWORD) == stringBuilderPin.toString().trim { it <= ' ' }) {
                    "$TAG, Match Pin:-${stringBuilderPin.toString().trim { it <= ' ' }}".logD()
                    goToNextScreen()
                }else {
                    "$TAG, Wrong Pin:-${stringBuilderPin.toString().trim { it <= ' ' }}".logD()
                    showToast(activity, resources.getString(R.string.wrong_password))
                    vibrateSmall(false)
                    clearPassword()
                }
            }
        } else {
            enableDisable(true)
        }
    }

    private fun goToNextScreen(){
        if (isFrom == AppConstant.BundleData.IS_FROM_SPLASH){
            startActivity(Intent(activity,SplashActivity::class.java))
            finish()
        }else {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    override fun onBackPressed() {
        if (isCreateNewPin){
            super.onBackPressed()
        }
    }

    private fun clearPassword() {
        stringBuilderPin.setLength(0)
        stringBuilderPin = java.lang.StringBuilder()
        setDotCount(0)
        enableDisable(true)
    }

    private fun setDotCount(dotCount: Int) {
        binding?.apply {
            "$TAG, DotCount:-$dotCount".logD()
            when (dotCount) {
                0 -> {
                    imageviewPincount0.hide()
                    imageviewPincount1.hide()
                    imageviewPincount2.hide()
                    imageviewPincount3.hide()
                }
                1 -> {
                    imageviewPincount0.show()
                    imageviewPincount1.hide()
                    imageviewPincount2.hide()
                    imageviewPincount3.hide()
                }
                2 -> {
                    imageviewPincount0.show()
                    imageviewPincount1.show()
                    imageviewPincount2.hide()
                    imageviewPincount3.hide()
                }
                3 -> {
                    imageviewPincount0.show()
                    imageviewPincount1.show()
                    imageviewPincount2.show()
                    imageviewPincount3.hide()
                }
                4 -> {
                    imageviewPincount0.show()
                    imageviewPincount1.show()
                    imageviewPincount2.show()
                    imageviewPincount3.show()
                }
            }
        }
    }

    open fun vibrateSmall(success: Boolean?) {
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        shake.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                changeModeToCreate(isCreateNewPin)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        if (!success!!) {
            dialogBinding?.dialogLogo?.startAnimation(shake)
        }
        val v = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                activity?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            v.vibrate(100)
        }
    }

    private fun doBiometricWork() {
        biometricIsWorkOrNot(activity) { isWorking: Boolean,hasFingerprintLockAvailable:Boolean, message: String ->
            if (isWorking){
                if (hasFingerprintLockAvailable){
                    startBiometricScan()
                    showBioDialog()
                }else{
                    finish()
                }
            }else{
                finish()
            }
        }
    }

    private fun startBiometricScan() {
        val keyguardManager = getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        val fingerprintManager = getSystemService(FINGERPRINT_SERVICE) as FingerprintManager
        if (!fingerprintManager.isHardwareDetected) {
            println("mnb::fingerprint no HardwareDetected")
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.USE_FINGERPRINT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                println("mnb::fingerprint no permission")
            } else {
                if (!fingerprintManager.hasEnrolledFingerprints()) {
                    println("mnb::fingerprint no registered fingerprint")
                } else {
                    if (!keyguardManager.isKeyguardSecure) {
                        println("mnb::fingerprint Lock screen security not enabled in Settings")
                    } else {
                        generateKey()
                        if (cipherInit()) {
                            val cryptoObject: FingerprintManager.CryptoObject? =
                                cipher?.let { FingerprintManager.CryptoObject(it) }
                            val helper = FingerprintHandler(activity)
                            helper.startAuth(fingerprintManager, cryptoObject)
                        }
                    }
                }
            }
        }
    }

    open fun cipherInit(): Boolean {
        try {
            cipher =
                Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        } catch (e: NoSuchAlgorithmException) {
            throw java.lang.RuntimeException("Failed to get Cipher", e)
        } catch (e: NoSuchPaddingException) {
            throw java.lang.RuntimeException("Failed to get Cipher", e)
        }
        return try {
            keyStore?.load(null)
            val key = keyStore?.getKey(keySoreName, null) as SecretKey
            cipher?.init(Cipher.ENCRYPT_MODE, key)
            true
        } catch (e: Exception) {
            when (e) {
                is KeyPermanentlyInvalidatedException -> {
                    false
                }
                is KeyStoreException, is CertificateException, is UnrecoverableKeyException, is IOException, is NoSuchAlgorithmException, is InvalidKeyException -> {
                    throw java.lang.RuntimeException("Failed to init Cipher", e)
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val keyGenerator: KeyGenerator = try {
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get KeyGenerator instance", e)
        } catch (e: NoSuchProviderException) {
            throw RuntimeException("Failed to get KeyGenerator instance", e)
        }
        try {
            keyStore?.load(null)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(
                    keySoreName,
                    KeyProperties.PURPOSE_ENCRYPT or
                            KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                        KeyProperties.ENCRYPTION_PADDING_PKCS7
                    )
                    .build()
            )
            keyGenerator.generateKey()
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        } catch (e: InvalidAlgorithmParameterException) {
            throw RuntimeException(e)
        } catch (e: CertificateException) {
            throw RuntimeException(e)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}