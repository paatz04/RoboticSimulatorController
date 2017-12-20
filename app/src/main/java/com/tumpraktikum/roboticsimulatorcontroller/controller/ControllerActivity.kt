package com.tumpraktikum.roboticsimulatorcontroller.controller

import android.annotation.SuppressLint
import android.content.Context
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
        mPresenter.setMotionDetector(mSensorManager)
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
    override fun setBody(value: Float) {
        tvValueBody.text = "Body: " + value
    }

    @SuppressLint("SetTextI18n")
    override fun setRotation(value: Float) {
        tvValueRotation.text = "Rotation: " + value
    }
}
