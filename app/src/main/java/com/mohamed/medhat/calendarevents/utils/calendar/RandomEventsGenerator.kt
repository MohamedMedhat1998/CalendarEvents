package com.mohamed.medhat.calendarevents.utils.calendar

import com.mohamed.medhat.calendarevents.model.CalendarEvent
import java.util.*
import javax.inject.Inject
import kotlin.random.Random

/**
 * A helper class to generate multiple events in the next week.
 */
class RandomEventsGenerator @Inject constructor() {

    /**
     * Generates a list of [CalendarEvent] objects with random timing during the next week.
     * @param n The number of objects to generate.
     * @return A list of randomly generated [CalendarEvent] objects.
     */
    fun generateRandomEvents(n: Int): List<CalendarEvent> {
        val events = mutableListOf<CalendarEvent>()

        var sDate: Calendar
        var sHour: Int

        var eDate: Calendar
        var eHour: Int

        repeat((1..n).count()) {
            sDate = Calendar.getInstance()
            sHour = (9..16).random(Random(System.currentTimeMillis()))
            sDate.add(Calendar.DATE, (1..7).random(Random(System.currentTimeMillis())))
            sDate.set(Calendar.HOUR_OF_DAY, sHour)

            eDate = Calendar.getInstance()
            eHour = ((sHour + 1)..17).random(Random(System.currentTimeMillis()))
            eDate.set(Calendar.DATE, sDate.get(Calendar.DATE))
            eDate.set(Calendar.HOUR_OF_DAY, eHour)

            events.add(
                CalendarEvent(
                    sDate.timeInMillis,
                    eDate.timeInMillis,
                    Calendar.getInstance().timeZone.id
                )
            )
        }
        return events
    }
}