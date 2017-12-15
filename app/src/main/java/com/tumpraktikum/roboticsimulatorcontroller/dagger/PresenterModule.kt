package com.tumpraktikum.roboticsimulatorcontroller.dagger

import com.tumpraktikum.roboticsimulatorcontroller.presenter.MainPresenter
import dagger.Module
import dagger.Provides

/**
 * Created by patriccorletto on 12/15/17.
 */
@Module
class PresenterModule {
    @Provides
    //@Singleton
    fun provideMainPresenter(): MainPresenter = MainPresenter()
}