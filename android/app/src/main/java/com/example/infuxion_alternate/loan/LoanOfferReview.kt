package com.example.infuxion_alternate.loan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.core.content.ContextCompat
import com.example.infuxion_alternate.R
import com.example.infuxion_alternate.Sanction

class LoanOfferReview : AppCompatActivity() {
    lateinit var btnNext : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_offer_review)
        supportActionBar?.hide()

        //Changing color of status bar
        val window: Window = this@LoanOfferReview.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this@LoanOfferReview, R.color.white)

        //To Sanction
        btnNext = findViewById(R.id.btnNext)
        btnNext.setOnClickListener() {
            var intent = Intent(this, Sanction::class.java)
            startActivity(intent)
        }

    }
}
