package com.mohamed.medhat.calendarevents.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mohamed.medhat.calendarevents.R
import dagger.hilt.android.AndroidEntryPoint

/**
 * Represents the main screen of the app that holds the fragments of the app.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}