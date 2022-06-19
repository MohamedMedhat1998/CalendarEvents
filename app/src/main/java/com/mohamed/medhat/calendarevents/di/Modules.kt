package com.mohamed.medhat.calendarevents.di

import android.content.ContentResolver
import android.content.Context
import com.mohamed.medhat.calendarevents.utils.calendar.CalendarHelper
import com.mohamed.medhat.calendarevents.utils.calendar.ContentProviderCalendarHelper
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

/**
 * Tells hilt how to provide some dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object Modules {

    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }

}

/**
 * Tells hilt how to bind specific dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class Binders {
    @Binds
    @ProviderCalendarHelper
    abstract fun bindCalendarHelper(contentProviderCalendarHelper: ContentProviderCalendarHelper): CalendarHelper
}

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class ProviderCalendarHelper