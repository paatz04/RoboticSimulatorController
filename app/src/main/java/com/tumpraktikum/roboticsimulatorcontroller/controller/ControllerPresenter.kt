package com.tumpraktikum.roboticsimulatorcontroller.controller

import android.hardware.SensorManager
import android.telecom.Call
import com.tumpraktikum.roboticsimulatorcontroller.controller.sensors.CallerSensorHandler
import com.tumpraktikum.roboticsimulatorcontroller.controller.sensors.SensorHandler
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import javax.inject.Inject

class ControllerPresenter
@Inject
constructor(private val myBluetoothManager: MyBluetoothManager)
    : ControllerContract.Presenter, CallerSensorHandler {

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
        mSensorHandler = SensorHandler(this, sensorManager)
    }

    override fun onResume() {
        mSensorHandler.onResume()
    }

    override fun onPause() {
        mSensorHandler.onPause()
    }

    override fun onChangeXAxis(newXValue : Float) {
        mView?.setXValue(newXValue)
    }

    override fun onChangeYAxis(newYValue : Float) {
        mView?.setYValue(newYValue)
    }

    override fun onChangeZAxis(newZValue : Float) {
        mView?.setZValue(newZValue)
    }
}