package com.tumpraktikum.roboticsimulatorcontroller.dagger

import android.content.Context
import com.tumpraktikum.roboticsimulatorcontroller.controller.ControllerPresenter
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import com.tumpraktikum.roboticsimulatorcontroller.main.MainPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class PresenterModule {
    @Provides
    @Singleton
    fun provideMainPresenter(myBluetoothManager: MyBluetoothManager,context: Context):
            MainPresenter = MainPresenter(myBluetoothManager,context)

    @Provides
    @Singleton
    fun provideControllerPresenter(myBluetoothManager: MyBluetoothManager):
            ControllerPresenter = ControllerPresenter(myBluetoothManager)
}