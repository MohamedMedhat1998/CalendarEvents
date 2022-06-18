package com.mohamed.medhat.calendarevents.ui.main

import android.os.Bundle
import com.mohamed.medhat.calendarevents.databinding.ActivityMainBinding
import com.mohamed.medhat.calendarevents.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Represents the main screen of the app that holds the fragments of the app.
 */
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}