package com.tumpraktikum.roboticsimulatorcontroller.controller
import android.hardware.SensorManager
import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.*

interface ControllerContract {

    interface View : BaseView<Presenter> {
        fun showEmptyView()
    }

    interface Presenter : BasePresenter<View> {
        fun setSensorManager(sensorManager: SensorManager)
        fun onResume()
        fun onPause()
    }
}