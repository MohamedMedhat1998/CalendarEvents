package com.mohamed.medhat.calendarevents.ui.main

import android.Manifest
import android.graphics.Color
import android.provider.CalendarContract
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.medhat.calendarevents.R
import com.mohamed.medhat.calendarevents.di.ProviderCalendarHelper
import com.mohamed.medhat.calendarevents.ui.BaseActivity
import com.mohamed.medhat.calendarevents.utils.Constants
import com.mohamed.medhat.calendarevents.utils.calendar.CalendarHelper
import com.mohamed.medhat.calendarevents.utils.calendar.RandomEventsGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

/**
 * An mvvm [ViewModel] for the [MainActivity].
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    @ProviderCalendarHelper private val calendarHelper: CalendarHelper,
    private val eventsGenerator: RandomEventsGenerator
) :
    ViewModel() {

    var calendarId = -1L

    /**
     * Runs the basic initializations for the app's calendar to work.
     * @param activity The activity hosting the initialization process.
     * @param onInitializationCompleted An optional lambda that will be executed after initialization.
     */
    fun initCalendar(activity: BaseActivity, onInitializationCompleted: suspend () -> Unit = {}) {
        withCalendarPermissions(activity) {
            viewModelScope.launch {
                val queriedCalendarId = calendarHelper.checkCalendar(
                    Constants.CALENDAR_NAME,
                    Constants.CALENDAR_OWNER_ACCOUNT
                )
                if (queriedCalendarId == -1L) {
                    val calendarId = calendarHelper.createCalendar(
                        accountName = Constants.CALENDAR_ACCOUNT_NAME,
                        accountType = CalendarContract.ACCOUNT_TYPE_LOCAL,
                        name = Constants.CALENDAR_NAME,
                        calendarDisplayName = Constants.CALENDAR_DISPLAY_NAME,
                        calendarColor = Color.CYAN,
                        calendarAccessLevel = CalendarContract.Calendars.CAL_ACCESS_READ,
                        ownerAccount = Constants.CALENDAR_OWNER_ACCOUNT
                    )
                    this@MainViewModel.calendarId = calendarId
                } else {
                    this@MainViewModel.calendarId = queriedCalendarId
                }
                onInitializationCompleted.invoke()
                Log.d(TAG, "initCalendar: id: $calendarId")
            }
        }
    }

    /**
     * Creates random events in the calendar.
     * @param activity The activity hosting the creation process.
     */
    fun createRandomEvents(activity: BaseActivity) {
        withCalendarPermissions(activity) {
            viewModelScope.launch {
                val events = eventsGenerator.generateRandomEvents(10)
                if (calendarId == -1L) {
                    initCalendar(activity) {
                        calendarHelper.createEvents(calendarId, *events.toTypedArray())
                    }
                } else {
                    calendarHelper.createEvents(calendarId, *events.toTypedArray())
                }
            }
        }
    }

    /**
     * Deletes all the events created associated with the apps calendar.
     * @param activity The activity hosting the deletion process.
     */
    fun deleteEvents(activity: BaseActivity) {
        withCalendarPermissions(activity) {
            viewModelScope.launch {
                calendarHelper.deleteAllEvents(calendarId)
            }
        }
    }

    /**
     * Executes the passed lambda if the calendar permissions are granted.
     */
    private fun withCalendarPermissions(activity: BaseActivity, lambda: () -> Unit) {
        activity.requirePermissions(
            arrayOf(
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR
            ),
            arrayOf(
                activity.getString(R.string.read_calendar_permission_name),
                activity.getString(R.string.write_calendar_permission_name)
            )
        ) {
            lambda.invoke()
        }
    }
}