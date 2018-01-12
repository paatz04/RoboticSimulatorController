package com.tumpraktikum.roboticsimulatorcontroller.controller

import android.hardware.SensorManager
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.ButtonManager
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.CallerButtonManager
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.enums.RobotControlButton
import com.tumpraktikum.roboticsimulatorcontroller.controller.sensors.CallerMotionDetector
import com.tumpraktikum.roboticsimulatorcontroller.controller.sensors.MotionDetector
import com.tumpraktikum.roboticsimulatorcontroller.controller.dataconverter.TransferDataConverter
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothService
import javax.inject.Inject

class ControllerPresenter
@Inject
constructor(mBluetoothManager: MyBluetoothManager)
    : ControllerContract.Presenter, CallerMotionDetector, CallerButtonManager {

    private var mView: ControllerContract.View? = null

    private lateinit var mMotionDetector: MotionDetector
    private var mButtonManager : ButtonManager = ButtonManager(this)

    private var mBluetoothService: MyBluetoothService? = mBluetoothManager.getService()


    override fun activateMotionDetector() {
        mMotionDetector.activateSensorManager()
    }

    override fun deactivateMotionDetector() {
        mMotionDetector.deactivateSensorManager()
    }

    override fun cancelBluetoothService() {
        mBluetoothService?.close()
    }

    override fun takeView(view: ControllerContract.View) {
        mView = view
    }

    override fun setMotionDetector(sensorManager: SensorManager) {
        mMotionDetector = MotionDetector(this, sensorManager)
    }

    override fun onChangeXAxis(newXValue : Float) {
        mView?.setBody(newXValue)
        mBluetoothService?.write(TransferDataConverter.getStringForBody(newXValue))
    }

    override fun onChangeYAxis(newYValue : Float) {
        mView?.setRotation(newYValue)
        mBluetoothService?.write(TransferDataConverter.getStringForRotation(newYValue))
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
        mBluetoothService?.write(TransferDataConverter.getStringForTip(newTipValue))
    }

    override fun onChangeGrabValue(newGrabValue : Float) {
        mView?.setGrab(newGrabValue)
        mBluetoothService?.write(TransferDataConverter.getStringForGrab(newGrabValue))
    }

}