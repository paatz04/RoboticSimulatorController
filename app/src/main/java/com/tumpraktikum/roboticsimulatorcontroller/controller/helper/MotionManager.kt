package com.tumpraktikum.roboticsimulatorcontroller.controller.helper

import com.tumpraktikum.roboticsimulatorcontroller.controller.helper.enums.RobotControlButtons

class MotionManager {
    fun buttonTapped(btn: RobotControlButtons) {
        TODO("not implemented")
    }

    fun buttonReleased(btn: RobotControlButtons) {
        TODO("not implemented")
    }

    fun setGravitySensor() {
        /*
         * Zu Beginn nur diesen Sensor verwenden
         * (Im Pflichtenheft ist zwar angegeben, dass wir den Roboter rotieren, indem wir auch das
         *  Smartphone rotieren, einfacher und auch intuitiver wäre jedoch den Roboter durch seitliches
         *  Kippen des Samrtphones zu rotieren)
         * - X-Achse für Tip
         * - Y-Achse für Rotierung
         */
        TODO("not implemented")
    }

    fun onPause() {
        TODO("not implemented - deactivate Sensors")
    }

    fun onResume() {
        TODO("not implemented - activate Sensors")
    }
}