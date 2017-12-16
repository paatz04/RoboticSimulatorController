package com.tumpraktikum.roboticsimulatorcontroller.dagger

import com.tumpraktikum.roboticsimulatorcontroller.controller.ControllerPresenter
import com.tumpraktikum.roboticsimulatorcontroller.helper.MotionManager
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import com.tumpraktikum.roboticsimulatorcontroller.main.MainPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class PresenterModule {
    @Provides
    @Singleton
    fun provideMainPresenter(myBluetoothManager: MyBluetoothManager):
            MainPresenter = MainPresenter(myBluetoothManager)

    @Provides
    @Singleton
    fun provideControllerPresenter(myBluetoothManager: MyBluetoothManager,
                                   motionManager: MotionManager):
            ControllerPresenter = ControllerPresenter(myBluetoothManager, motionManager)
}