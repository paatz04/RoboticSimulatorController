package com.tumpraktikum.roboticsimulatorcontroller.controller

import android.content.Context
import android.hardware.SensorManager
import com.tumpraktikum.roboticsimulatorcontroller.controller.helper.enums.GravitySensorEventListener
import com.tumpraktikum.roboticsimulatorcontroller.controller.helper.enums.SensorHandler
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import javax.inject.Inject

class ControllerPresenter
@Inject
constructor(private val myBluetoothManager: MyBluetoothManager)
    : ControllerContract.Presenter {

    private var mView: ControllerContract.View? = null
    private lateinit var mSensorHandler : SensorHandler


    override fun takeView(view: ControllerContract.View) {
        mView = view
    }

    override fun dropView() {
        mView = null
    }

    override fun setSensorManager(sensorManager: SensorManager) {
        // TODO: realize with dagger
        mSensorHandler = SensorHandler(sensorManager)
    }

    override fun onResume() {
        mSensorHandler.onResume()
    }

    override fun onPause() {
        mSensorHandler.onPause()
    }
}