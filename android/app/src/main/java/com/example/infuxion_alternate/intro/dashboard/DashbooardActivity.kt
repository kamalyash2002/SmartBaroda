package com.example.infuxion_alternate.intro.dashboard


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.infuxion_alternate.R
import com.example.infuxion_alternate.databinding.ActivitySplashBinding
import com.example.infuxion_alternate.utils.TinyDB
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class DashbooardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashbooard)

    }

}