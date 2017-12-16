package com.tumpraktikum.roboticsimulatorcontroller.dagger

import com.tumpraktikum.roboticsimulatorcontroller.helper.MotionManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MotionModule {
    @Provides
    @Singleton
    fun providesMotionManager(): MotionManager = MotionManager()
}