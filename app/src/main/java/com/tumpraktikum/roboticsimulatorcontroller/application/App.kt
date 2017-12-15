package com.tumpraktikum.roboticsimulatorcontroller.application

import android.app.Application
import com.tumpraktikum.roboticsimulatorcontroller.dagger.AppComponent
import com.tumpraktikum.roboticsimulatorcontroller.dagger.AppModule
import com.tumpraktikum.roboticsimulatorcontroller.dagger.DaggerAppComponent

/**
 * Created by patriccorletto on 12/15/17.
 */
class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
    }

    private fun initDagger(app: App): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()
}
