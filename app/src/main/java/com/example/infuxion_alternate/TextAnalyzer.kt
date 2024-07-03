import android.annotation.SuppressLint
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextAnalyzer : ImageAnalysis.Analyzer {

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        // Convert the image to an InputImage
        val image = InputImage.fromMediaImage(imageProxy.image as Image, imageProxy.imageInfo.rotationDegrees)


        // Pass the image to the ML Kit text recognition API
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)


            .addOnSuccessListener { visionText ->
                val text = visionText.text
                Log.d(TAG, "Analyzed text: $text")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Text recognition failed", e)
            }
            .addOnCompleteListener {
                // Close the image proxy when processing is complete
                imageProxy.close()
            }
    }

    companion object {
        private const val TAG = "TextAnalyzer"
    }
}
