package com.tumpraktikum.roboticsimulatorcontroller.controller

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.tumpraktikum.roboticsimulatorcontroller.R
import com.tumpraktikum.roboticsimulatorcontroller.application.App
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.enums.RobotControlButton.*
import kotlinx.android.synthetic.main.activity_controller.*
import javax.inject.Inject

class ControllerActivity : AppCompatActivity(), ControllerContract.View {

    @Inject
    lateinit var mPresenter: ControllerPresenter

    // Create a BroadcastReceiver for ACTION_FOUND AND ACTION_STATE_CHANGED.
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            mPresenter.bluetoothActionFound(context, intent)
        }
    }

    private val mSensorManager: SensorManager by lazy {
        // by lazy this code is only executed the first time
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        (application as App).appComponent.inject(this)
        mPresenter.setMotionDetector(mSensorManager)

        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        registerReceiver(mReceiver, filter)

        btnGrab.setOnTouchListener { _, motionEvent -> onTouchGrab(motionEvent) }
        btnRelease.setOnTouchListener { _, motionEvent -> onTouchReleased(motionEvent) }
        btnTipUp.setOnTouchListener { _, motionEvent -> onTouchTipUp(motionEvent) }
        btnTipDown.setOnTouchListener { _, motionEvent -> onTouchTipDown(motionEvent) }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.activateMotionDetector()
        mPresenter.takeView(this)
        mPresenter.updateBluetoothHandler()
    }

    override fun onPause() {
        super.onPause()
        mPresenter.deactivateMotionDetector()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.cancelBluetoothService()
        unregisterReceiver(mReceiver)

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

    override fun close() {
        finish()
    }

    override fun showEmptyView() {
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun setBody(value: Float) {
        tvValueBody.text = getString(R.string.body_dynamic, value)
    }

    override fun setRotation(value: Float) {
        tvValueRotation.text = getString(R.string.rotation_dynamic, value)
    }

    override fun setTip(value: Float) {
        tvValueTip.text = getString(R.string.tip_dynamic, value)
    }

    override fun setGrab(value: Float) {
        tvValueGrab.text = getString(R.string.grab_dynamic, value)
    }

    override fun setColorGrab(color: String) {
        tvColorGrab.text = getString(R.string.grabbed_color_dynamic, color)
    }

    override fun setScoreBlue(score: Int) {
        tvScoreBlue.text = getString(R.string.blue_score_dynamic, score)
    }

    override fun setScoreGreen(score: Int) {
        tvScoreGreen.text = getString(R.string.green_score_dynamic, score)
    }

    override fun setScoreRed(score: Int) {
        tvScoreRed.text = getString(R.string.red_score_dynamic, score)
    }

    override fun setScoreMissed(score: Int) {
        tvScoreMissed.text = getString(R.string.missed_dynamic, score)
    }
}
