package com.example.infuxion_alternate.intro

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.infuxion_alternate.databinding.ActivitySecurityBinding

class SecurityActivity : AppCompatActivity() {

    private var binding: ActivitySecurityBinding? = null
    private var activity: Activity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecurityBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        activity = this
        overridePendingTransition(0, 0)

        init()
    }



    private fun init() {

        binding?.apply {
            activity?.let { activity->
                processFurther.setOnClickListener {
//                    startActivity(Intent(activity, AllowPermissions::class.java))
                }
            }
        }
    }
}