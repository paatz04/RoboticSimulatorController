package com.tumpraktikum.roboticsimulatorcontroller.controller

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tumpraktikum.roboticsimulatorcontroller.R
import com.tumpraktikum.roboticsimulatorcontroller.application.App
import kotlinx.android.synthetic.main.activity_controller.*
import javax.inject.Inject

class ControllerActivity : AppCompatActivity(), ControllerContract.View {

    @Inject lateinit var mPresenter: ControllerPresenter

    private val mSensorManager : SensorManager by lazy {
        // by lazy this code is only executed the first time
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        (application as App).appComponent.inject(this)
        mPresenter.setSensorManager(mSensorManager)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.onResume()
        mPresenter.takeView(this)
    }

    override fun onPause() {
        super.onPause()
        mPresenter.onPause()
    }

    override fun showEmptyView() {
        TODO("not implemented")
    }

    @SuppressLint("SetTextI18n")
    override fun setXValue(x: Float) {
        sensorX.text = "x: " + x
    }

    @SuppressLint("SetTextI18n")
    override fun setYValue(y: Float) {
        sensorY.text = "y: " + y
    }

    @SuppressLint("SetTextI18n")
    override fun setZValue(z: Float) {
        sensorZ.text = "z: " + z
    }
}
