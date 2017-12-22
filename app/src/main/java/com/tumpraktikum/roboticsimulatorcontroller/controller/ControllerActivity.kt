package com.tumpraktikum.roboticsimulatorcontroller.controller

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.tumpraktikum.roboticsimulatorcontroller.R
import com.tumpraktikum.roboticsimulatorcontroller.application.App
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.enums.RobotControlButton.*
import kotlinx.android.synthetic.main.activity_controller.*
import javax.inject.Inject

class ControllerActivity : AppCompatActivity(), ControllerContract.View{

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

        btnGrab.setOnTouchListener { _, motionEvent ->  onTouchGrab(motionEvent) }
        btnRelease.setOnTouchListener { _, motionEvent -> onTouchReleased(motionEvent) }
        btnTipUp.setOnTouchListener{ _, motionEvent -> onTouchTipUp(motionEvent) }
        btnTipDown.setOnTouchListener { _, motionEvent ->  onTouchTipDown(motionEvent) }
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

    private fun onTouchGrab(motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN)
            mPresenter.onButtonClicked(GRAB)
        if (motionEvent.action == MotionEvent.ACTION_UP)
            mPresenter.onButtonReleased(GRAB)
        return false
    }

    private fun onTouchReleased(motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN)
            mPresenter.onButtonClicked(RELEASE)
        if (motionEvent.action == MotionEvent.ACTION_UP)
            mPresenter.onButtonReleased(RELEASE)
        return false
    }

    private fun onTouchTipUp(motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN)
            mPresenter.onButtonClicked(TIP_UP)
        if (motionEvent.action == MotionEvent.ACTION_UP)
            mPresenter.onButtonReleased(TIP_UP)
        return false
    }

    private fun onTouchTipDown(motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == MotionEvent.ACTION_DOWN)
            mPresenter.onButtonClicked(TIP_DOWN)
        if (motionEvent.action == MotionEvent.ACTION_UP)
            mPresenter.onButtonReleased(TIP_DOWN)
        return false
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

    @SuppressLint("SetTextI18n")
    override fun setTip(value: Float) {
        tvValueTip.text = "Tip: " + value
    }

    @SuppressLint("SetTextI18n")
    override fun setGrab(value: Float) {
        tvValueGrab.text = "Grab: " + value
    }
}
