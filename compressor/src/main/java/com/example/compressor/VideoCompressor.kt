
package com.example.compressor

import com.arthenica.ffmpegkit.FFmpegKit

object VideoCompressor {
    fun compressVideo(inputPath: String, outputPath: String, callback: (Boolean, String?) -> Unit) {
        val command = "-i $inputPath -vcodec libx264 -crf 28 $outputPath"
        FFmpegKit.executeAsync(command) { session ->
            val returnCode = session.returnCode
            if (returnCode.isValueSuccess) {
                callback(true, null)
            } else {
                callback(false, session.failStackTrace)
            }
        }
    }
}
