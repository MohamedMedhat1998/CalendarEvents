package com.mohamed.medhat.calendarevents.utils.calendar

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.provider.CalendarContract
import androidx.core.database.getLongOrNull
import com.mohamed.medhat.calendarevents.model.CalendarEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val CALENDAR_ID_COLUMN_INDEX = 0;
private const val EVENT_ID_COLUMN_INDEX = 0;

/**
 * A [CalendarHelper] implementation that depends on the calendar's [ContentProvider] to perform its job.
 */
class ContentProviderCalendarHelper @Inject constructor(private val contentResolver: ContentResolver) :
    CalendarHelper {
    override suspend fun checkCalendar(calendarName: String, ownerAccount: String): Long {
        return withContext(Dispatchers.IO) {
            val uri = CalendarContract.Calendars.CONTENT_URI
            val selection =
                "(${CalendarContract.Calendars.NAME} = ?) AND (${CalendarContract.Calendars.OWNER_ACCOUNT} = ?)"
            val selectionArgs: Array<String> = arrayOf(calendarName, ownerAccount)
            val cursor = contentResolver.query(
                uri,
                arrayOf(CalendarContract.Calendars._ID),
                selection,
                selectionArgs,
                null
            )
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    val calendarId = cursor.getLongOrNull(CALENDAR_ID_COLUMN_INDEX)
                    if (calendarId != null) {
                        cursor.close()
                        return@withContext calendarId
                    }
                    cursor.close()
                    return@withContext -1
                }
                cursor.close()
                return@withContext -1
            }
            return@withContext -1
        }
    }

    override suspend fun createCalendar(
        accountName: String,
        accountType: String,
        name: String,
        calendarDisplayName: String,
        calendarColor: Int,
        calendarAccessLevel: Int,
        ownerAccount: String
    ): Long {
        return withContext(Dispatchers.IO) {
            val insertionUri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, accountType).build()

            val values = ContentValues().apply {
                put(CalendarContract.Calendars.ACCOUNT_NAME, accountName)
                put(CalendarContract.Calendars.ACCOUNT_TYPE, accountType)
                put(CalendarContract.Calendars.NAME, name)
                put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, calendarDisplayName)
                put(CalendarContract.Calendars.CALENDAR_COLOR, calendarColor)
                put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, calendarAccessLevel)
                put(CalendarContract.Calendars.OWNER_ACCOUNT, ownerAccount)
                put(CalendarContract.Calendars.VISIBLE, 1)
            }

            val uri = contentResolver.insert(insertionUri, values)
            if (uri != null) {
                val id = uri.lastPathSegment
                if (id != null) {
                    return@withContext id.toLong()
                }
                return@withContext -1
            }
            return@withContext -1
        }
    }

    override suspend fun createEvents(calendarId: Long, vararg events: CalendarEvent) {
        withContext(Dispatchers.IO) {
            val contentValues = events.map {
                ContentValues().apply {
                    put(CalendarContract.Events.DTSTART, it.dtStart)
                    put(CalendarContract.Events.DTEND, it.dtEnd)
                    put(CalendarContract.Events.TITLE, "Random Event Title")
                    put(CalendarContract.Events.DESCRIPTION, "Random Event Description")
                    put(CalendarContract.Events.EVENT_TIMEZONE, it.eventTimezone)
                    put(CalendarContract.Events.CALENDAR_ID, calendarId)
                }
            }
            contentResolver.bulkInsert(
                CalendarContract.Events.CONTENT_URI,
                contentValues.toTypedArray()
            )
        }
    }

    override suspend fun deleteAllEvents(calendarId: Long) {
        withContext(Dispatchers.IO) {
            val cursor = contentResolver.query(
                CalendarContract.Events.CONTENT_URI,
                arrayOf(CalendarContract.Events._ID),
                "${CalendarContract.Events.CALENDAR_ID} = ?",
                arrayOf(calendarId.toString()),
                null
            )
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    contentResolver.delete(
                        CalendarContract.Events.CONTENT_URI,
                        "${CalendarContract.Events._ID} = ?",
                        arrayOf(cursor.getLong(EVENT_ID_COLUMN_INDEX).toString())
                    )
                }
                cursor.close()
            }
        }
    }
}