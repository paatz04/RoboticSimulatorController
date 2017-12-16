package com.tumpraktikum.roboticsimulatorcontroller.dagger

import com.tumpraktikum.roboticsimulatorcontroller.controller.ControllerActivity
import com.tumpraktikum.roboticsimulatorcontroller.main.MainActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AppModule::class,
    PresenterModule::class,
    BluetoothModule::class,
    MotionModule::class
])
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: ControllerActivity)
}