package com.tumpraktikum.roboticsimulatorcontroller.dagger

import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import com.tumpraktikum.roboticsimulatorcontroller.main.MainPresenter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by patriccorletto on 12/15/17.
 */
@Module
class PresenterModule {
    @Provides
    @Singleton
    fun provideMainPresenter(myBluetoothManager: MyBluetoothManager): MainPresenter = MainPresenter(myBluetoothManager)
}