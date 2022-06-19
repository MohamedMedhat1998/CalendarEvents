package com.mohamed.medhat.calendarevents.ui.main

import android.os.Bundle
import android.provider.CalendarContract
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mohamed.medhat.calendarevents.R
import com.mohamed.medhat.calendarevents.databinding.FragmentMainBinding
import com.mohamed.medhat.calendarevents.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Represents the screen where the user can create/delete calendar events.
 */
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        initViews()
    }

    /**
     * Initializes the UI.
     */
    private fun initViews() {
        binding.btnMainGenerateEvents.setOnClickListener {
            mainViewModel.createRandomEvents(requireActivity() as BaseActivity)
        }
        binding.btnMainDeleteEvents.setOnClickListener {
            mainViewModel.deleteEvents(requireActivity() as BaseActivity)
        }
    }
}