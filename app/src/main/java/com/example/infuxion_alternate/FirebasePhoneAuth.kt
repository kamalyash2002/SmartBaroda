package com.example.infuxion_alternate

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.verifyPhoneNumber
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class FirebasePhoneAuth : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var  verificationId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_phone_auth)


        auth = FirebaseAuth.getInstance()
            // Get the user's phone number
            val phoneNumber = "+918445214758"

            // Set up the options for the SMS verification
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)  // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout duration
                .setActivity(this) // Activity to handle the flow
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                        // Auto-retrieval or SMS-based verification completed successfully
                        // Use `credential` to sign in the user
                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        Log.d(TAG, "onVerificationFailed: try again")                    }

                    override fun onCodeSent(
                        p0: String,
                        p1: PhoneAuthProvider.ForceResendingToken
                    ) {

                        super.onCodeSent(p0, p1)
                        verificationId = p0
                        // Code sent successfully
                        // Save the verification ID and token to use later
                        // ...
                    }
                }).build()

            PhoneAuthProvider.verifyPhoneNumber(options)


            val credential = PhoneAuthProvider.getCredential(verificationId, "878979")

            auth.signInWithCredential(credential)
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        Log.d(TAG, "sendVerificationCode: complete")
                    }
                    else
                    {
                        Log.d(TAG, "sendVerificationCode: failed")

                    }
                }
        }
    }