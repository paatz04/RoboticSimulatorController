package com.tumpraktikum.roboticsimulatorcontroller.dagger

import com.tumpraktikum.roboticsimulatorcontroller.view.ControllerActivity
import com.tumpraktikum.roboticsimulatorcontroller.view.MainActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by patriccorletto on 12/15/17.
 */
@Singleton
@Component(modules = [AppModule::class, PresenterModule::class, BluetoothModule::class])
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: ControllerActivity)

}