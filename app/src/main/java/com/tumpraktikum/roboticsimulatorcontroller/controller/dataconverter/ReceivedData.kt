package com.tumpraktikum.roboticsimulatorcontroller.controller.dataconverter

class ReceivedData(private var mSensor: RoboticSensorPart, private var mValue: Int) {

    fun getRoboticSensorPart(): RoboticSensorPart {
        return mSensor
    }

    fun getValue(): Int {
        return mValue
    }

}