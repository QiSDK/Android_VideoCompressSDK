
package com.example.compressor

import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

object ImageCompressor {
    fun compressImage(inputFile: File, outputFile: File, quality: Int = 80): Boolean {
        val bitmap = BitmapFactory.decodeFile(inputFile.absolutePath)
        val outputStream = FileOutputStream(outputFile)
        val result = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        outputStream.flush()
        outputStream.close()
        return result
    }
}
