package com.example.infuxion_alternate.loan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.core.content.ContextCompat
import com.example.infuxion_alternate.R

class LoanOffers : AppCompatActivity() {

    lateinit var spLoanOffer: Spinner
    lateinit var spInterest: Spinner
    lateinit var btnNext: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_offers)

        //Changing color of status bar
        val window: Window = this@LoanOffers.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this@LoanOffers, R.color.white)

        spLoanOffer = findViewById(R.id.spLoanOffer)
        val options = listOf("₹3000")
//        val loanAm = arrayOf<String?>()
        val arrayAdapter: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(this, R.layout.spinnner_itemlist, options)

        arrayAdapter.setDropDownViewResource(R.layout.spinnner_itemlist)
        spLoanOffer.adapter = arrayAdapter


        spInterest = findViewById(R.id.spInterest)
        val loanIn = arrayOf<String?>(
            "a% for 4W, b% for 4W, and c% for 5W",
            "x% for 4W, y% for 4W, and z% for 5W"
        )
        val arrAdapter: ArrayAdapter<Any?> =
            ArrayAdapter<Any?>(this, R.layout.spinnner_itemlist, loanIn)
        arrAdapter.setDropDownViewResource(R.layout.spinner_loan_interest_items)
        spInterest.adapter = arrAdapter

        btnNext = findViewById(R.id.btnNext)

        //To Loan Review
        btnNext.setOnClickListener() {
            val intent = Intent(this, LoanOfferReview::class.java)
            startActivity(intent)
        }
    }
}
