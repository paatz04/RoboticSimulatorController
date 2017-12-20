package com.tumpraktikum.roboticsimulatorcontroller.controller.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log

class GravitySensorEventListener(private var mSensorHandler: SensorHandler)
    : SensorEventListener {

    private val xAxis: Int = 0
    private val yAxis: Int = 1
    private val zAxis: Int = 2

    private var mXValue: Float = 0F
    private var mYValue: Float = 0F
    private var mZValue: Float = 0F


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) { }

    override fun onSensorChanged(event : SensorEvent) {
        val values = event.values
        if (values[xAxis] != mXValue) mSensorHandler.onChangeXAxis(values[xAxis])
        if (values[yAxis] != mYValue) mSensorHandler.onChangeYAxis(values[yAxis])
        if (values[zAxis] != mZValue) mSensorHandler.onChangeZAxis(values[zAxis])
    }
}