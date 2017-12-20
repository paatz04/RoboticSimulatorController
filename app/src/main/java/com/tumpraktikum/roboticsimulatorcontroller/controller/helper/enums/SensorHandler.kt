package com.tumpraktikum.roboticsimulatorcontroller.controller.helper.enums

import android.hardware.Sensor
import android.hardware.SensorManager


class SensorHandler(private var mSensorManager: SensorManager) {
    private var mGravitySensor : Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    private var mGravitySensorEventListener : GravitySensorEventListener = GravitySensorEventListener(this)
    private val mGravityIntensity : Int = SensorManager.SENSOR_DELAY_NORMAL

    fun onResume() {
        mSensorManager.registerListener(mGravitySensorEventListener, mGravitySensor, mGravityIntensity)
    }

    fun onPause() {
        mSensorManager.unregisterListener(mGravitySensorEventListener)
    }

    fun onChangeXAxis(newValue: Float) {
        TODO("not Implemented")
    }
    fun onChangeYAxis(newValue: Float) {
        TODO("not Implemented")
    }
    fun onChangeZAxis(newValue: Float) {
        TODO("not Implemented")
    }
}