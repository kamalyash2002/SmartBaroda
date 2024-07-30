package com.example.infuxion_alternate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.infuxion_alternate.loan.LoanOffers

class CheckedEligibility : AppCompatActivity() {

    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checked_eligibility)
        overridePendingTransition(0, 0)

        btnNext = findViewById(R.id.btnNext)

        btnNext.setOnClickListener(){
            val intent = Intent(this, LoanOffers::class.java)
            startActivity(intent)
            finish()
        }
    }
}