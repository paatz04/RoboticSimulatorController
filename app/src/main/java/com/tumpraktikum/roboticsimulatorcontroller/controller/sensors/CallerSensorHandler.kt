package com.tumpraktikum.roboticsimulatorcontroller.controller.sensors

interface CallerSensorHandler {
    fun onChangeXAxis(newXValue : Float)
    fun onChangeYAxis(newYValue : Float)
    fun onChangeZAxis(newZValue : Float)
}