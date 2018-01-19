package com.tumpraktikum.roboticsimulatorcontroller.controller

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.os.Handler
import android.os.Message
import android.util.Log
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.ButtonManager
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.CallerButtonManager
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.enums.RobotControlButton
import com.tumpraktikum.roboticsimulatorcontroller.controller.dataconverter.ReceivedData
import com.tumpraktikum.roboticsimulatorcontroller.controller.dataconverter.RoboticSensorPart
import com.tumpraktikum.roboticsimulatorcontroller.controller.dataconverter.TransferDataConverter
import com.tumpraktikum.roboticsimulatorcontroller.controller.dataconverter.TransferDataConverterException
import com.tumpraktikum.roboticsimulatorcontroller.controller.sensors.CallerMotionDetector
import com.tumpraktikum.roboticsimulatorcontroller.controller.sensors.MotionDetector
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothService
import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.MessageConstants
import javax.inject.Inject

// ToDo init TextViews
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
                MessageConstants.MESSAGE_BLUETOOTH_CONNECTION_CLOSED -> mView.close()
                MessageConstants.MESSAGE_BLUETOOTH_MESSAGE -> handleBluetoothMessage(message.obj as String)
            }
            false
        })
    }

    private fun handleBluetoothMessage(message: String) {
        try {
            val receivedData: ReceivedData = TransferDataConverter.getReceivedData(message)
            when (receivedData.getRoboticSensorPart()) {
                RoboticSensorPart.COLOR_GRAB -> mView.setColorGrab(TransferDataConverter.getColor(receivedData.getValue()))
                RoboticSensorPart.SCORE_RED -> mView.setScoreRed(receivedData.getValue())
                RoboticSensorPart.SCORE_BLUE -> mView.setScoreBlue(receivedData.getValue())
                RoboticSensorPart.SCORE_GREEN -> mView.setScoreGreen(receivedData.getValue())
                RoboticSensorPart.SCORE_MISSED -> mView.setScoreMissed(receivedData.getValue())
            }
        } catch (e: TransferDataConverterException) {
            mView.showToast(e.message)
        }
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

    override fun bluetoothActionFound(context: Context, intent: Intent) {
        val action = intent.action
        when ( action) {
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
                when(state)
                {
                    BluetoothAdapter.STATE_OFF, BluetoothAdapter.STATE_TURNING_OFF -> {
                        mView.close()
                    }
                }
            }
        }
    }

}