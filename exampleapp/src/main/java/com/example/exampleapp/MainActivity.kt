
package com.example.exampleapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.compressor.ImageCompressor
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.compressButton)
        val textView = findViewById<TextView>(R.id.resultText)

        button.setOnClickListener {
            val inputFile = File(filesDir, "input.jpg")
            val outputFile = File(filesDir, "compressed.jpg")
            if (ImageCompressor.compressImage(inputFile, outputFile)) {
                textView.text = "Compressed successfully: ${outputFile.length() / 1024} KB"
            } else {
                textView.text = "Compression failed."
            }
        }
    }
}
