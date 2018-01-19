package com.tumpraktikum.roboticsimulatorcontroller.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/*
The AppModule defines the Application context, that is injected when context is required.
 */
@Module
class AppModule(private val app: Application) {
    @Provides
    @Singleton
    fun provideContext(): Context = app
}