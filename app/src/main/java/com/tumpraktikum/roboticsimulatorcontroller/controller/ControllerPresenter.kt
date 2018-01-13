package com.tumpraktikum.roboticsimulatorcontroller.controller

import android.hardware.SensorManager
import android.os.Handler
import android.os.Message
import android.util.Log
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.ButtonManager
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.CallerButtonManager
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.enums.RobotControlButton
import com.tumpraktikum.roboticsimulatorcontroller.controller.sensors.CallerMotionDetector
import com.tumpraktikum.roboticsimulatorcontroller.controller.sensors.MotionDetector
import com.tumpraktikum.roboticsimulatorcontroller.controller.dataconverter.TransferDataConverter
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothService
import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.MessageConstants
import javax.inject.Inject

class ControllerPresenter
@Inject
constructor(private var mBluetoothManager: MyBluetoothManager)
    : ControllerContract.Presenter, CallerMotionDetector, CallerButtonManager {

    private lateinit var mView: ControllerContract.View

    private lateinit var mMotionDetector: MotionDetector
    private var mButtonManager: ButtonManager = ButtonManager(this)

    private var mBluetoothService: MyBluetoothService = mBluetoothManager.getService()


    override fun updateBluetoothHandler() {
        mBluetoothService.updateHandler(getBluetoothHandler())
    }

    override fun cancelBluetoothService() {
        mBluetoothService.close()
    }

    private fun getBluetoothHandler(): Handler {
        return Handler(Handler.Callback {
            message: Message? ->
            when (message?.what) {
                MessageConstants.MESSAGE_TOAST -> mView.showToast( message.data?.getString("toast") ?: "message is null")
                MessageConstants.MESSAGE_BLUETOOTH_CONNECTION_CLOSED -> closeView()
            }
            false
        })
    }

    private fun closeView() {
        Log.d("ControllerPresenter", "close View")
        mView.close()
    }

    override fun takeView(view: ControllerContract.View) {
        mView = view
        mBluetoothService = mBluetoothManager.getService()
    }

    override fun activateMotionDetector() {
        mMotionDetector.activateSensorManager()
    }

    override fun deactivateMotionDetector() {
        mMotionDetector.deactivateSensorManager()
    }

    override fun setMotionDetector(sensorManager: SensorManager) {
        mMotionDetector = MotionDetector(this, sensorManager)
    }

    override fun onChangeXAxis(newXValue: Float) {
        mView.setBody(newXValue)
        mBluetoothService.write(TransferDataConverter.getStringForBody(newXValue))
    }

    override fun onChangeYAxis(newYValue: Float) {
        mView.setRotation(newYValue)
        mBluetoothService.write(TransferDataConverter.getStringForRotation(newYValue))
    }

    override fun onChangeZAxis(newZValue: Float) { }

    override fun onButtonClicked(clickedButton: RobotControlButton) {
        mButtonManager.onButtonClicked(clickedButton)
    }

    override fun onButtonReleased(releasedButton: RobotControlButton) {
        mButtonManager.onButtonReleased(releasedButton)
    }

    override fun onChangeTipValue(newTipValue: Float) {
        mView.setTip(newTipValue)
        mBluetoothService.write(TransferDataConverter.getStringForTip(newTipValue))
    }

    override fun onChangeGrabValue(newGrabValue : Float) {
        mView.setGrab(newGrabValue)
        mBluetoothService.write(TransferDataConverter.getStringForGrab(newGrabValue))
    }

}