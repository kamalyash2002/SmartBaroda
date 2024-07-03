package com.example.infuxion_alternate

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.CheckBox
import android.widget.ScrollView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat


class SanctionLoanContract : AppCompatActivity() {


    private lateinit var checkBoxTnc: CheckBox
    private lateinit var btnAllowTnc: Button
    private lateinit var scrollView: ScrollView
    private var isScrolledToBottom = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sanction_loan_contract)
        supportActionBar?.hide()

        //Changing color of status bar
        val window: Window = this@SanctionLoanContract.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(this@SanctionLoanContract, R.color.white)

        val builder = AlertDialog.Builder(this)
            .setTitle("Sanction")
            .setMessage("Please read till the bottom of this page before proceeding.")
            .setIcon(R.drawable.ic_saturn)
            .setPositiveButton("Accept") { _, _ ->

            }

        checkBoxTnc = findViewById(R.id.cbTnc)
        btnAllowTnc = findViewById(R.id.btnAllowTnc)
        scrollView = findViewById(R.id.svTnc)


        //VALIDATION
        checkBoxTnc.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (isChecked) {
                    btnAllowTnc.isEnabled = true
                    btnAllowTnc.backgroundTintList =
                        AppCompatResources.getColorStateList(this, R.color.white)
                    btnAllowTnc?.setTextColor(ContextCompat.getColor(this, R.color.black))
                } else {
                    btnAllowTnc.isEnabled = false
                    btnAllowTnc.backgroundTintList =
                        AppCompatResources.getColorStateList(this, R.color.greybg)
                    btnAllowTnc?.setTextColor(ContextCompat.getColor(this, R.color.grey))

                }

            }

            scrollView.viewTreeObserver
                .addOnScrollChangedListener {
                    if (scrollView.getChildAt(0).bottom
                        <= scrollView.height + scrollView.scrollY
                    ) {
                        isScrolledToBottom = true
                    }
                }

            //Showing alert + Switching intent | Tnc => Privacy Policy
            btnAllowTnc.setOnClickListener() {
                if (btnAllowTnc.isEnabled && isScrolledToBottom) {
//                    val intent = Intent(this, SanctionOtp::class.java)
//                    startActivity(intent)
                }
                if (btnAllowTnc.isEnabled && !isScrolledToBottom) {
                    builder.show()
                }
            }
        }
    }
}