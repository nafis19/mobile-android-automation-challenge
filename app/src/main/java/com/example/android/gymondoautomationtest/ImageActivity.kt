package com.example.android.gymondoautomationtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import java.util.concurrent.locks.Lock

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val textview: AppCompatTextView = findViewById(R.id.item_text)
        val itemName = intent.extras?.getString("Item name")

        textview.text = itemName
    }
}