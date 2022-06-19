package com.mohamed.medhat.calendarevents.model

/**
 * Represents an event of a calendar
 * @param dtStart The time the event starts in UTC millis since epoch.
 * @param dtEnd The time the event ends in UTC millis since epoch.
 * @param eventTimezone The timezone for the event.
 */
data class CalendarEvent(val dtStart: Long, val dtEnd: Long, val eventTimezone: String)