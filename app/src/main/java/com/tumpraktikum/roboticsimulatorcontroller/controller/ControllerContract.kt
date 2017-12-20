package com.tumpraktikum.roboticsimulatorcontroller.controller
import android.hardware.SensorManager
import com.tumpraktikum.roboticsimulatorcontroller.controller.helper.enums.RobotControlButtons
import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.*

interface ControllerContract {

    interface View : BaseView<Presenter> {
        fun showEmptyView()
        fun setBody(value: Float)
        fun setRotation(value: Float)
    }

    interface Presenter : BasePresenter<View> {
        fun onResume()
        fun onPause()
        fun setMotionDetector(sensorManager: SensorManager)
        fun onButtonClicked(clickedButton : RobotControlButtons)
        fun onButtonReleased(releasedButton : RobotControlButtons)
    }
}