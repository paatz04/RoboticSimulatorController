package com.tumpraktikum.roboticsimulatorcontroller.dagger

import com.tumpraktikum.roboticsimulatorcontroller.controller.ControllerActivity
import com.tumpraktikum.roboticsimulatorcontroller.bluetooth.ConnectThread
import com.tumpraktikum.roboticsimulatorcontroller.main.MainActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AppModule::class,
    PresenterModule::class,
    BluetoothModule::class]
    )

/*
This is the only Dagger component that we use, because of the simplicity of this application.
It Injects the presenters into the according views and also provides android context where needed
 */
interface AppComponent {
    fun inject(target: MainActivity)
    fun inject(target: ControllerActivity)
    fun inject(target: ConnectThread)
}