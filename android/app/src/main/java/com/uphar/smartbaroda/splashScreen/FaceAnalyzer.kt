package com.uphar.smartbaroda.splashScreen

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Handler
import android.util.Log
import android.util.Size
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.lifecycle.Lifecycle
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.uphar.bussinesss.domain.dataStore.basePreference.BasePreferencesManager
import com.uphar.bussinesss.domain.dataStore.basePreference.BasePreferencesManagerImpl
import com.uphar.smartbaroda.MainActivity
import com.uphar.smartbaroda.R
import com.uphar.smartbaroda.ui.LoginActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.firstOrNull


class FaceAnalyzer(context: Context, lifecycle: Lifecycle, private val overlay: Overlay) : ImageAnalysis.Analyzer {

    private val tvStatus:TextView = (context as Activity).findViewById(R.id.tvStatus)
    private val tvStep: TextView = (context as Activity).findViewById(R.id.tvStep)

    private val ivStepOne: ImageView = (context as Activity).findViewById(R.id.stepOne)
    private val ivStepTwo: ImageView = (context as Activity).findViewById(R.id.stepTwo)
    private val ivStepThree: ImageView = (context as Activity).findViewById(R.id.stepThree)
//    private val ivStepFour: ImageView = (context as Activity).findViewById(R.id.stepFour)


    var blinkToastShown = false
    var leftToastShown = false
    var rightToastShown = false
    private var count :Int = 0

    private var blinkSuccess :Boolean = false
    private var inside :Boolean = false
    private var headTurnLeftSuccess :Boolean = false
    private var headTurnRightSuccess :Boolean = false








    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .setMinFaceSize(0.15f)
        //.enableTracking() //disable when contour is enable https://developers.google.com/ml-kit/vision/face-detection/android
        .build()

    private val detector = FaceDetection.getClient(options)

    init {
        //add the detector in lifecycle observer to properly close it when it's no longer needed.
        lifecycle.addObserver(detector)
    }

    override fun analyze(imageProxy: ImageProxy) {
        overlay.setPreviewSize(Size(imageProxy.width,imageProxy.height))
        detectFaces(imageProxy)
    }

    private val successListener = OnSuccessListener<List<Face>> { faces ->


        for (face in faces) {



            // Step 1 -> Blink Eyes.

            if (!blinkSuccess){


                val leftEyeOpenProbability = face.leftEyeOpenProbability
                val rightEyeOpenProbability = face.rightEyeOpenProbability
                if (rightEyeOpenProbability != null) {
                    if (leftEyeOpenProbability != null) {
                        if (leftEyeOpenProbability < 0.5 && rightEyeOpenProbability < 0.5) {
                            count+=1
                            tvStatus.text="Scanning for blinking eyes.."

                            if(count ==5){

                                blinkSuccess = true

                                if (!blinkToastShown) {
                                Toast.makeText(
                                    context, "Blinking Test is successful!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                    blinkToastShown = true
                                }
                                tvStep.text="Turn your head 30 degrees left"
                                tvStatus.text="Scanning for movement.."

                                ivStepOne.setImageResource(R.drawable.ic_successtick)

                            }
                        }
                    }
                }
                else{
                    tvStatus.text="Unable to detect eyes"
                }
            }

            //Step 2  -> Turn head left
            if( blinkSuccess and !headTurnLeftSuccess) {
                if (face.headEulerAngleY <= -30) {
                    // User has rotated their head 30 degrees to the left
                    if(!leftToastShown) {
                        headTurnLeftSuccess = true
                        Toast.makeText(
                            context, "Left head movement has been registered!",
                            Toast.LENGTH_SHORT
                        ).show()
                        leftToastShown = true
                    }
                    tvStep.text="Turn your head 30 degrees right"
                    tvStatus.text="Scanning for movement.."

                    ivStepTwo.setImageResource(R.drawable.ic_successtick)
                    val handler = Handler()
                    val runnable = Runnable {
                        headTurnLeftSuccess = true
                    }
                    handler.postDelayed(runnable, 2000)
                }
                else{
                    tvStatus.text="No movement detected.."
                }
            }

            //Step 3  -> Turn head right
            if( blinkSuccess and headTurnLeftSuccess and !headTurnRightSuccess) {
                if (face.headEulerAngleY >= 30) {
                    // User has rotated their head 30 degrees to the right
                    if(!rightToastShown) {
                        headTurnRightSuccess = true
                        Toast.makeText(
                            context, "Right head movement has been registered!",
                            Toast.LENGTH_SHORT
                        ).show()
                        rightToastShown = true
                    }
                    tvStep.text="Face detection is successful"
                    tvStatus.text="All steps are successful!"
                    ivStepThree.setImageResource(R.drawable.ic_successtick)
                    val handler = Handler()
                    val runnable = Runnable {

                        //j remove
                        /*val intent = Intent(context, ReviewRegisterDetails::class.java)
                        context.startActivity(intent)*/

                        // j add
                        val basePreferencesManager: BasePreferencesManager by lazy {
                            BasePreferencesManagerImpl(context)
                        }
                        GlobalScope.launch {
                            val accessToken = basePreferencesManager.getAccessToken().firstOrNull()
                            if (accessToken.isNullOrEmpty()) {
                                val intent = Intent(context, LoginActivity::class.java)
                                context.startActivity(intent)
                                (context as Activity).finish()

                            } else {
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                (context as Activity).finish()
                            }

                        }

                    }
                    handler.postDelayed(runnable, 2000)




                }
                else{
                    tvStatus.text="No movement detected.."
                }
            }
        }

//     if(faces.size == 1){
//
//     }
//    else if(faces.size == 0){
//
//     }
//    else{
//
//     }
overlay.setFaces(faces)
    }

    private val failureListener = OnFailureListener { e ->
        Log.e(TAG, "Face analysis failure.", e)
    }

    @SuppressLint("UnsafeExperimentalUsageError", "UnsafeOptInUsageError")
    private fun detectFaces(imageProxy: ImageProxy) {
        val image = InputImage.fromMediaImage(imageProxy.image as Image, imageProxy.imageInfo.rotationDegrees)
        detector.process(image)
            .addOnSuccessListener(successListener)
            .addOnFailureListener(failureListener)
            .addOnCompleteListener{
                imageProxy.close()
            }
    }




    companion object {
        private const val TAG = "FaceAnalyzer"
    }
}