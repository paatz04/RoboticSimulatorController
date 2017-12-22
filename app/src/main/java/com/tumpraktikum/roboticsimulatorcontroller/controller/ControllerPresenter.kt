package com.tumpraktikum.roboticsimulatorcontroller.controller

import android.hardware.SensorManager
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.ButtonManager
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.CallerButtonManager
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.enums.RobotControlButton
import com.tumpraktikum.roboticsimulatorcontroller.controller.sensors.CallerMotionDetector
import com.tumpraktikum.roboticsimulatorcontroller.controller.sensors.MotionDetector
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import javax.inject.Inject

class ControllerPresenter
@Inject
constructor(private val myBluetoothManager: MyBluetoothManager)
    : ControllerContract.Presenter, CallerMotionDetector, CallerButtonManager{

    private var mView: ControllerContract.View? = null
    private lateinit var mMotionDetector: MotionDetector
    private var mButtonManager : ButtonManager = ButtonManager(this)


    override fun onResume() {
        mMotionDetector.onResume()
    }

    override fun onPause() {
        mMotionDetector.onPause()
    }

    override fun takeView(view: ControllerContract.View) {
        mView = view
    }

    override fun dropView() {
        mView = null
    }

    override fun setMotionDetector(sensorManager: SensorManager) {
        // TODO: realize with dagger
        mMotionDetector = MotionDetector(this, sensorManager)
    }

    override fun onChangeXAxis(newXValue : Float) {
        mView?.setBody(newXValue)
    }

    override fun onChangeYAxis(newYValue : Float) {
        mView?.setRotation(newYValue)
    }

    override fun onChangeZAxis(newZValue: Float) { }

    override fun onButtonClicked(clickedButton: RobotControlButton) {
        mButtonManager.onButtonClicked(clickedButton)
    }

    override fun onButtonReleased(releasedButton: RobotControlButton) {
        mButtonManager.onButtonReleased(releasedButton)
    }

    override fun onChangeTipValue(newTipValue: Float) {
        mView?.setTip(newTipValue)
    }

    override fun onChangeGrabValue(newGrabValue : Float) {
        mView?.setGrab(newGrabValue)
    }

}