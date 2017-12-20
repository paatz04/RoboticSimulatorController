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

    var mSensorEventListener =  object : SensorEventListener {
        override fun onAccuracyChanged(sensor : Sensor, accuracy : Int) {}

        @SuppressLint("SetTextI18n")
        override fun onSensorChanged(event : SensorEvent) {
            val values = event.values
            sensorX.text = "x: " + values[0]
            sensorY.text = "y: " + values[1]
            sensorZ.text = "z: " + values[2]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        (application as App).appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.takeView(this)


    }

    override fun onPause() {
        super.onPause()

        mSensorManager.unregisterListener(mSensorEventListener)
    }

    override fun showEmptyView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
