package com.example.exampleapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.compressor.ImageCompressor
import com.example.compressor.VideoCompressor
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var originalSizeText: TextView
    private lateinit var compressedSizeText: TextView
    private var selectedUri: Uri? = null
    private var isImage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val selectImageBtn = findViewById<Button>(R.id.selectImageButton)
        val compressImageBtn = findViewById<Button>(R.id.compressImageButton)
        val selectVideoBtn = findViewById<Button>(R.id.selectVideoButton)
        val compressVideoBtn = findViewById<Button>(R.id.compressVideoButton)
        originalSizeText = findViewById(R.id.originalSizeText)
        compressedSizeText = findViewById(R.id.compressedSizeText)

        selectImageBtn.setOnClickListener {
            isImage = true
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1001)
        }

        compressImageBtn.setOnClickListener {
            selectedUri?.let {
                val input = File(it.path!!)
                originalSizeText.text = "原始大小: ${input.length() / 1024} KB"
                val output = File(cacheDir, "compressed.jpg")
                ImageCompressor.compressImage(input, output)
                compressedSizeText.text = "压缩后大小: ${output.length() / 1024} KB"
            }
        }

        selectVideoBtn.setOnClickListener {
            isImage = false
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 1002)
        }

        compressVideoBtn.setOnClickListener {
            selectedUri?.let {
                val input = File(it.path!!)
                originalSizeText.text = "原始大小: ${input.length() / 1024} KB"
                val output = File(cacheDir, "compressed.mp4")
                VideoCompressor.compressVideo(
                    inputPath = input.absolutePath,
                    outputPath = output.absolutePath
                ) { success, errorMsg ->
                    if (success) {
                        Log.d("VideoCompressor", "压缩成功，输出路径：${output.absolutePath}")
                        // TODO: 可以更新 UI 显示新视频大小
                    } else {
                        Log.e("VideoCompressor", "压缩失败：$errorMsg")
                    }
                }
                compressedSizeText.text = "压缩后大小: ${output.length() / 1024} KB"
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && data != null) {
//            selectedUri = data.data
//        }


        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectedUri = data.data
            if (selectedUri != null) {
                when (requestCode) {
                    REQUEST_CODE_PICK_IMAGE -> {
                        // 处理图片
                        val imagePath = getPathFromUri(selectedUri)
                        // TODO: 显示图片大小
                    }
                    REQUEST_CODE_PICK_VIDEO -> {
                        // 处理视频
                        val videoPath = getPathFromUri(selectedUri)
                        // TODO: 显示视频大小
                    }
                }
            }
        }
    }

    fun Context.getPathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return cursor.getString(columnIndex)
            }
        }
        return null
    }
}