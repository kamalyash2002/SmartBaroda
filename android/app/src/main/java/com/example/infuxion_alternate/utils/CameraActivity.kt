package com.example.infuxion_alternate.utils

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.example.infuxion_alternate.intro.dashboard.DashbooardActivity
import com.example.infuxion_alternate.databinding.ActivityCameraBinding
import java.io.File
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private var binding: ActivityCameraBinding? = null
    private var activity: Activity?=null
    private var TAG="CameraActivity"
    private lateinit var photoFilePath: String
    private var imageCapture: ImageCapture? = null

    private var cameraExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        overridePendingTransition(0, 0)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        activity = this

        init()
    }

    private fun init() {
        binding?.apply {
            activity?.let {
                cameraExecutor = Executors.newSingleThreadExecutor()

                imgCaptureBtn.setOnClickListener{
                    takePhoto()
                }

                startCamera()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        imageCapture = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).setFlashMode(ImageCapture.FLASH_MODE_AUTO)/*.setTargetResolution(Size(1200, 1600))*/.build()
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            val previewUseCase = Preview.Builder().build()
                .also {
                    it.setSurfaceProvider(binding?.viewFinder?.createSurfaceProvider())
                }
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this,cameraSelector,previewUseCase,imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "startCamera failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto(){
        imageCapture?.let{
            val fileName = "JPEG_${System.currentTimeMillis()}.jpg"
            val file = File(externalMediaDirs[0],fileName)
            val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
            it.takePicture(
                outputFileOptions,
                cameraExecutor,
                object : ImageCapture.OnImageSavedCallback {

//                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults){
//                        Log.i(TAG,"The image has been saved in ${file.toUri()}")
////                        runOnUiThread{
////                            Toast.makeText(activity, "Photo Store in :-${file.absolutePath}", Toast.LENGTH_LONG).show()
////                        }
//                        Handler(Looper.getMainLooper()).postDelayed({
//                            // todo (Store profile image in API)
//                            val intent = Intent(activity, ReviewRegisterDetails::class.java)
//                            startActivity(intent)
//                        },2000)
//                    }

                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        photoFilePath = file.absolutePath
                        Log.i(TAG, "The image has been saved in $photoFilePath")
                        Handler(Looper.getMainLooper()).postDelayed({
                            // Store the photoFilePath in a shared preference or pass it to the next activity
                            val intent = Intent(activity, DashbooardActivity::class.java)
                            intent.putExtra("photoPath", photoFilePath)
                            startActivity(intent)
                        }, 2000)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.d(TAG, "Error taking photo:$exception")
                        runOnUiThread {
                            Toast.makeText(activity, "Error taking photo", Toast.LENGTH_LONG).show()
                        }
                    }

                })
        }
    }
}