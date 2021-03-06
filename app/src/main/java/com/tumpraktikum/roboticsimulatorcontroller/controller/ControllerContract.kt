package com.tumpraktikum.roboticsimulatorcontroller.controller
import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.enums.RobotControlButton
import com.tumpraktikum.roboticsimulatorcontroller.interfaces.BasePresenter
import com.tumpraktikum.roboticsimulatorcontroller.interfaces.BaseView

interface ControllerContract {

    interface View : BaseView<Presenter> {
        fun close()
        fun showEmptyView()
        fun showToast(message: String)
        fun setBody(value: Float)
        fun setRotation(value: Float)
        fun setTip(value: Float)
        fun setGrab(value: Float)
        fun setColorGrab(color: String)
        fun setScoreRed(score: Int)
        fun setScoreGreen(score: Int)
        fun setScoreBlue(score: Int)
        fun setScoreMissed(score: Int)
    }

    interface Presenter : BasePresenter<View> {
        fun bluetoothActionFound(context: Context, intent: Intent)
        fun activateMotionDetector()
        fun deactivateMotionDetector()
        fun cancelBluetoothService()
        fun setMotionDetector(sensorManager: SensorManager)
        fun updateBluetoothHandler()
        fun onButtonClicked(clickedButton : RobotControlButton)
        fun onButtonReleased(releasedButton : RobotControlButton)
    }
}