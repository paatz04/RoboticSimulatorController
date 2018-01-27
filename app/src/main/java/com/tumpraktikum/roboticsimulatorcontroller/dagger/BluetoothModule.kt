package com.tumpraktikum.roboticsimulatorcontroller.dagger

import com.tumpraktikum.roboticsimulatorcontroller.bluetooth.MyBluetoothManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/*
This module defines the injection for the BluetoothManger
 */
@Module
class BluetoothModule {
    @Provides
    @Singleton
    fun providesBluetoothManager(): MyBluetoothManager = MyBluetoothManager()
}
