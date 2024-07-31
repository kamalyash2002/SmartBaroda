package com.uphar.smartbaroda.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import com.uphar.smartbaroda.MainActivity
import com.uphar.smartbaroda.databinding.ActivitySplashBinding
import com.uphar.smartbaroda.ui.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    private var binding: ActivitySplashBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        init()
    }

    private fun init() {
        Handler(Looper.getMainLooper()).postDelayed({
            goToNextScreen()
        }, SPLASH_DELAY) // Set the delay for the splash screen duration
    }

    private fun goToNextScreen() {
        val intent = Intent(this, FaceMlActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val SPLASH_DELAY = 4000L
    }
}
