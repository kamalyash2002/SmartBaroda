package com.uphar.smartbaroda.splashScreen

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.face.Face
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Canvas
import android.graphics.Paint
import android.hardware.Camera
import android.util.Size
import com.google.mlkit.vision.face.FaceLandmark


class Overlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var previewWidth: Int = 0
    private var widthScaleFactor = 1.0f
    private var previewHeight: Int = 0
    private var heightScaleFactor = 1.0f

    private var faces = emptyArray<Face>()
    private var orientation = Configuration.ORIENTATION_PORTRAIT

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
        style = Paint.Style.STROKE
        strokeWidth = 5.0f
    }

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
        style = Paint.Style.FILL
        strokeWidth = 1.0f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawOverlay(canvas);
    }

    fun setOrientation(orientation: Int)
    {
        this.orientation = orientation
    }

    fun setPreviewSize(size: Size) {
        // Need to swap width and height when in portrait, since camera's natural orientation is landscape.
        if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
            previewWidth = size.width
            previewHeight = size.height
        }
        else
        {
            previewWidth = size.height
            previewHeight = size.width
        }
    }

    fun setFaces(faceList: List<com.google.mlkit.vision.face.Face>)
    {
        faces = faceList.toTypedArray()
        postInvalidate()
    }

    private fun drawOverlay(canvas: Canvas) {
        widthScaleFactor = width.toFloat() / previewWidth
        heightScaleFactor = height.toFloat() / previewHeight

//        for(face in faces)
//        {
////            drawFaceBorder(face, canvas)
////            drawFaceContour(face,canvas)
//            //drawFaceLandmark(face,canvas)
//        }
    }

    private fun drawFaceBorder(face: Face, canvas: Canvas)
    {
        val bounds = face.boundingBox
        val left = translateX(bounds.left.toFloat())
        val top = translateY(bounds.top.toFloat())
        val right = translateX(bounds.right.toFloat())
        val bottom = translateY(bounds.bottom.toFloat())

        canvas.drawRect(left, top, right, bottom, paint)
    }

    //Note: Contours are detected for only the most prominent face in an image.
    private fun drawFaceContour(face: Face, canvas: Canvas)
    {
        val contour = face.allContours
        for (faceContour in contour) {
            for (point in faceContour.points) {
                val px = translateX(point.x)
                val py = translateY(point.y)
                canvas.drawCircle(px, py, 10.0f, dotPaint)
            }
        }
    }

    private fun drawFaceLandmark(face: Face, canvas: Canvas)
    {
        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)
        val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)
        val leftCheek = face.getLandmark(FaceLandmark.LEFT_CHEEK)
        val rightCheek = face.getLandmark(FaceLandmark.RIGHT_CHEEK)

        drawFaceLandmark(leftEye,canvas)
        drawFaceLandmark(rightEye,canvas)
        drawFaceLandmark(leftCheek,canvas)
        drawFaceLandmark(rightCheek,canvas)
    }

    private fun drawFaceLandmark(faceLandmark: FaceLandmark?, canvas: Canvas)
    {
        if(faceLandmark == null)
            return
        canvas.drawCircle(translateX(faceLandmark.position.x),translateY(faceLandmark.position.y), 10.0f, dotPaint)
    }

    private fun translateX(x: Float): Float = x * widthScaleFactor
    private fun translateY(y: Float): Float = y * heightScaleFactor

}