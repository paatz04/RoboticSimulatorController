package com.tumpraktikum.roboticsimulatorcontroller.controller.buttons

interface CallerButtonManager {
    fun onChangeTipValue(newTipValue : Float)
    fun onGrab()
    fun onRelease()
}