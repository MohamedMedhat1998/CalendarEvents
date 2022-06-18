package com.mohamed.medhat.calendarevents.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * An application class that is used in Hilt DI.
 * It can be used to load any required initial configurations.
 */
@HiltAndroidApp
class App : Application()