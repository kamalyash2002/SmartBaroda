package com.example.infuxion_alternate.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import com.example.infuxion_alternate.R
import java.io.File

object FileUtil {

    /**
     * compress image path
     */
    @JvmStatic
    fun getDestinationCompressImageFilePath(context: Context?, file: File): File? {
        val compressImageSave: File
        val path= context?.resources?.getString(R.string.app_name)+"/" + "CompressImage" + "/" +"Image_" + System.currentTimeMillis() +"."+ file.extension
        val parent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        else
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        compressImageSave = File(parent, path)

        if (!compressImageSave.exists()) {
            if (!compressImageSave.mkdirs()) {
                Log.d("DestinationCompressPath", "Failed to create directory")
                return null
            }
        }

        Log.d("DestinationCompressPath", compressImageSave.toString())
        return compressImageSave
    }
}