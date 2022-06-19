package com.mohamed.medhat.calendarevents.utils.calendar

import android.Manifest
import androidx.annotation.RequiresPermission

/**
 * A type that handles the calendar operations.
 */
interface CalendarHelper {

    /**
     * Checks for the existence of a specific calendar.
     * @param calendarName The name of the calendar.
     * @param ownerAccount The calendar's owner account.
     * @return The id of the calendar if found or -1 if it wasn't found.
     */
    suspend fun checkCalendar(calendarName: String, ownerAccount: String): Long

    /**
     * Creates a calendar.
     * @param accountName The account that was used to sync the entry to the device.
     * @param accountType The type of the account that was used to sync the entry to the device
     * @param name The name of the calendar.
     * @param calendarDisplayName The display name of the calendar.
     * @param calendarColor The color of the calendar.
     * @param calendarAccessLevel The level of access that the user has for the calendar.
     * @param ownerAccount The owner account for this calendar, based on the calendar feed.
     * @return The id of the newly created calendar, or -1 if the calendar wasn't created.
     */
    suspend fun createCalendar(
        accountName: String,
        accountType: String,
        name: String,
        calendarDisplayName: String,
        calendarColor: Int,
        calendarAccessLevel: Int,
        ownerAccount: String
    ): Long
}