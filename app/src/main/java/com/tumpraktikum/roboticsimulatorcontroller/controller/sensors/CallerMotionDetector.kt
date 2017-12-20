package com.tumpraktikum.roboticsimulatorcontroller.controller.sensors

interface CallerMotionDetector {
    fun onChangeXAxis(newXValue : Float)
    fun onChangeYAxis(newYValue : Float)
    fun onChangeZAxis(newZValue : Float)
}