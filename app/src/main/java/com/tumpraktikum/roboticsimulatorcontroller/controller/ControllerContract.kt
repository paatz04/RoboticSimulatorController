package com.tumpraktikum.roboticsimulatorcontroller.controller
import android.hardware.SensorManager
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.enums.RobotControlButton
import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.*

interface ControllerContract {

    interface View : BaseView<Presenter> {
        fun showEmptyView()
        fun setBody(value: Float)
        fun setRotation(value: Float)
        fun setTip(value: Float)
        fun setGrab(value: Float)
    }

    interface Presenter : BasePresenter<View> {
        fun activateMotionDetector()
        fun deactivateMotionDetector()
        fun cancelBluetoothService()
        fun setMotionDetector(sensorManager: SensorManager)
        fun onButtonClicked(clickedButton : RobotControlButton)
        fun onButtonReleased(releasedButton : RobotControlButton)
    }
}