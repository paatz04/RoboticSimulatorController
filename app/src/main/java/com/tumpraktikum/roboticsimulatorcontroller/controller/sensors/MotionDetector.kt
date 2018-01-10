package com.tumpraktikum.roboticsimulatorcontroller.controller.sensors

import android.hardware.Sensor
import android.hardware.SensorManager
import kotlin.math.abs
import kotlin.math.round

class MotionDetector(private var mCaller : CallerMotionDetector,
                     private var mSensorManager: SensorManager) {
    private val xAtLeastAwayFromZero : Float = 1F
    private val xIntensitySteps : Float = 0.5F
    private val yAtLeastAwayFromZero : Float = 1F
    private val yIntensitySteps : Float = 0.5F
    private val zAtLeastAwayFromZero : Float = 1F
    private val zIntensitySteps : Float = 0.5F

    private var mGravitySensor : Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    private var mGravitySensorEventListener : GravitySensorEventListener = GravitySensorEventListener(this)
    private val gravityIntensity: Int = SensorManager.SENSOR_DELAY_NORMAL

    private var mXValue: Float = 0F
    private var mYValue: Float = 0F
    private var mZValue: Float = 0F


    fun activateSensorManager() {
        mSensorManager.registerListener(mGravitySensorEventListener, mGravitySensor, gravityIntensity)
    }

    fun deactivateSensorManager() {
        mSensorManager.unregisterListener(mGravitySensorEventListener)
    }

    fun onChangeXAxis(newXValue: Float) {
        if (abs(newXValue) >= xAtLeastAwayFromZero) {
            val roundedNewXValue = roundForStepSize(newXValue, xIntensitySteps)
            if (roundedNewXValue != mXValue) {
                mXValue = roundedNewXValue
                mCaller.onChangeXAxis(mXValue)
            }
        }else{
            if (mXValue != 0F) {
                mXValue = 0F
                mCaller.onChangeXAxis(0F)
            }
        }
    }
    fun onChangeYAxis(newYValue: Float) {
        if (abs(newYValue) >= yAtLeastAwayFromZero) {
            val roundedNewYValue = roundForStepSize(newYValue, yIntensitySteps)
            if (roundedNewYValue != mYValue) {
                mYValue = roundedNewYValue
                mCaller.onChangeYAxis(mYValue)
            }
        }else{
            if (mYValue != 0F) {
                mYValue = 0F
                mCaller.onChangeYAxis(0F)
            }
        }
    }
    fun onChangeZAxis(newZValue: Float) {
        if (abs(newZValue) >= zAtLeastAwayFromZero) {
            val roundedNewZValue = roundForStepSize(newZValue, zIntensitySteps)
            if (roundedNewZValue != mZValue) {
                mZValue = roundedNewZValue
                mCaller.onChangeZAxis(mZValue)
            }
        }else{
            if (mYValue != 0F) {
                mYValue = 0F
                mCaller.onChangeZAxis(0F)
            }
        }
    }

    private fun roundForStepSize(value : Float, stepSize : Float): Float {
        return round(value/stepSize) * stepSize
    }
}