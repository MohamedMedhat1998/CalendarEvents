package com.mohamed.medhat.calendarevents.ui.main

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.mohamed.medhat.calendarevents.R
import com.mohamed.medhat.calendarevents.databinding.FragmentMainBinding
import com.mohamed.medhat.calendarevents.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

// Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
private val EVENT_PROJECTION: Array<String> = arrayOf(
    CalendarContract.Calendars._ID,                     // 0
    CalendarContract.Calendars.ACCOUNT_NAME,            // 1
    CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,   // 2
    CalendarContract.Calendars.OWNER_ACCOUNT            // 3
)

// The indices for the projection array above.
private const val PROJECTION_ID_INDEX: Int = 0
private const val PROJECTION_ACCOUNT_NAME_INDEX: Int = 1
private const val PROJECTION_DISPLAY_NAME_INDEX: Int = 2
private const val PROJECTION_OWNER_ACCOUNT_INDEX: Int = 3

private const val TAG = "MainFragment"

/**
 * Represents the screen where the user can create/delete calendar events.
 */
@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)
        // TODO remove this code.
        binding.button.setOnClickListener {
            (requireActivity() as BaseActivity).requirePermissions(
                arrayOf(
                    Manifest.permission.READ_CALENDAR,
                    Manifest.permission.WRITE_CALENDAR
                ),
                arrayOf("Read Calendar", "Write Calendar")
            ) {
                // Run query
                val uri: Uri = CalendarContract.Calendars.CONTENT_URI

                val cur = requireActivity().contentResolver.query(
                    uri,
                    EVENT_PROJECTION,
                    null,
                    null,
                    null
                )
                // Use the cursor to step through the returned records
                if (cur != null) {
                    while (cur.moveToNext()) {
                        // Get the field values
                        val calID = cur.getLong(PROJECTION_ID_INDEX)
                        val displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX)
                        val accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX)
                        val ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX)
                        // Do something with the values...
                        Log.d(TAG, "onViewCreated: calID: $calID")
                        Log.d(TAG, "onViewCreated: displayName: $displayName")
                        Log.d(TAG, "onViewCreated: accountName: $accountName")
                        Log.d(TAG, "onViewCreated: ownerName: $ownerName")
                    }
                }
            }
        }
    }
}