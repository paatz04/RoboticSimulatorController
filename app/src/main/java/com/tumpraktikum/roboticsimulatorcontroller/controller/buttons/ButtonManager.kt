package com.tumpraktikum.roboticsimulatorcontroller.controller.buttons

import android.util.Log
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.enums.RobotGrabState
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.enums.RobotGrabState.*
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.enums.RobotArmStates
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.enums.RobotArmStates.*
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.enums.RobotControlButton
import com.tumpraktikum.roboticsimulatorcontroller.controller.buttons.enums.RobotControlButton.*

/*
 * Sobald geklickt wird bekommt der Button/Robotorarm eine gewisse Geschwindikeit. Diese behält
 * er so lange, bis der button Wieder losgelassen wird
 */
class ButtonManager(private  val caller : CallerButtonManager) {

    private val speedTip : Float = 3F
    private val speeGrab : Float = 3F

    private var mStateTip : RobotArmStates = ARM_PAUSED
    private var mStateRobotGrab: RobotGrabState = GRAB_PAUSED

    fun onButtonClicked(clickedButton : RobotControlButton) {
        Log.d("ButtonManager", "onButtonClicked " + clickedButton)
        when (clickedButton) {
            TIP_UP -> onTipUpButtonClicked()
            TIP_DOWN -> onTipDownButtonClicked()
            GRAB -> onGrabButtonClicked()
            RELEASE -> onReleaseButtonClicked()
        }
    }

    private fun onTipUpButtonClicked() {
        mStateTip = UP
        caller.onChangeTipValue(speedTip)
    }

    private fun onTipDownButtonClicked() {
        mStateTip = DOWN
        caller.onChangeTipValue(-speedTip)
    }

    private fun onGrabButtonClicked() {
        mStateRobotGrab = GRABING
        caller.onChangeGrabValue(speeGrab)
    }

    private fun onReleaseButtonClicked() {
        mStateRobotGrab = RELEASEING
        caller.onChangeGrabValue(-speeGrab)
    }

    fun onButtonReleased(releasedButton : RobotControlButton) {
        Log.d("ButtonManager", "onButtonReleased " + releasedButton)
        when (releasedButton) {
            TIP_UP -> onTipUpButtonReleased()
            TIP_DOWN -> onTipDownButtonReleased()
            GRAB -> onGrabButtonReleased()
            RELEASE -> onReleaseButtonReleased()
        }
    }

    private fun onTipUpButtonReleased() {
        if (mStateTip == UP)
            stopTip()
    }
    private fun onTipDownButtonReleased() {
        if (mStateTip == DOWN)
            stopTip()
    }

    private fun stopTip() {
        mStateTip = ARM_PAUSED
        caller.onChangeTipValue(0F)
    }

    private fun onGrabButtonReleased() {
        if (mStateRobotGrab == GRABING)
            stopGrab()
    }

    private fun onReleaseButtonReleased() {
        if (mStateRobotGrab == RELEASEING)
            stopGrab()
    }

    private fun stopGrab() {
        mStateRobotGrab = GRAB_PAUSED
        caller.onChangeGrabValue(0F)
    }
}