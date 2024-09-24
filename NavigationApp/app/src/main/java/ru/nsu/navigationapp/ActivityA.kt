package ru.nsu.navigationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActivityA : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)
    }
}