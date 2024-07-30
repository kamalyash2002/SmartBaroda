package com.example.infuxion_alternate


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat



class Eligibility : AppCompatActivity() {


    private var progressBar: ProgressBar? = null
    private var i = 0
    private var txtView: TextView? = null
    lateinit var textview : TextView
    lateinit var textProcess : TextView
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eligibility)
        supportActionBar?.hide()


        //Changing color of status bar
        val window: Window = this@Eligibility.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor( this@Eligibility, R.color.white)


        txtView = findViewById(R.id.text_view)
        textProcess = findViewById(R.id.textProcess)

        val progressBar = findViewById<ProgressBar>(R.id.progress_Bar) as ProgressBar

        i = progressBar.progress

        Thread(Runnable {
            // this loop will run until the value of i becomes 99
            while (i < 100) {
                i += 1
                // Update the progress bar and display the current value
                handler.post(Runnable {
                    progressBar.progress = i
//                    // setting current progress to the textview
                    txtView = findViewById(R.id.text_view)
                    txtView!!.text = i.toString() + "%"
                    if (i==30){
                        textProcess.text="Congratulations! Your Identity proof is verified! Woah! Took just three seconds!!"
                    } else if (i==50){
                        textProcess.text="Woah! That was blazingly fast! Your criminal record is all clean"

                    }else if (i==70){
                        textProcess.text="Hmmm. Your credit report seems all fine. Good job!"

                    }
                    else if (i==100){
                        textProcess.text="Congrats! You have successfully passed all the levels! That too, just in 100secs!"

                    }
                })
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }


            val intent = Intent(this, CheckedEligibility::class.java)
            startActivity(intent)
        }).start()

    }
}