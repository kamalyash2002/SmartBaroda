package com.example.infuxion_alternate.intro
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.result.contract.ActivityResultContracts
import com.example.infuxion_alternate.databinding.ActivitySplashBinding
import com.example.infuxion_alternate.utils.AppConstant
import com.example.infuxion_alternate.utils.MethodMaster
import com.example.infuxion_alternate.utils.TinyDB
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private var binding: ActivitySplashBinding? = null
    private var activity: Activity? = null

    private val sharedPrefFile = "tempPref"
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var tinyDB: TinyDB? = null

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            goToNextScreen()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        supportActionBar?.hide()

        activity = this
        tinyDB = TinyDB(this)
        firebaseAnalytics = Firebase.analytics

        init()
    }

    private fun init() {
        Handler(Looper.getMainLooper()).postDelayed({
            tinyDB?.putBoolean(AppConstant.Prefs.SPLASH_COMPLETE_WATCH,true)
            if (MethodMaster.checkLockIsEnable(tinyDB)){
                val intent = Intent(this, FaceMlActivity::class.java)
                resultLauncher.launch(intent)
            }else{
                goToNextScreen()
            }
        }, if (tinyDB?.getBoolean(AppConstant.Prefs.SPLASH_COMPLETE_WATCH)==true) 4000 else 6000)
    }

    private fun goToNextScreen() {


        val intent = Intent(this, FaceMlActivity::class.java)
        startActivity(intent)
        finish()

    }
}