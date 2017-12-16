package com.tumpraktikum.roboticsimulatorcontroller.dagger

import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BluetoothModule {
    @Provides
    @Singleton
    fun providesBluetoothManager(): MyBluetoothManager = MyBluetoothManager()
}
