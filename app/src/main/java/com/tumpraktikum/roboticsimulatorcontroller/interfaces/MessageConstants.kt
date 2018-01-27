package com.tumpraktikum.roboticsimulatorcontroller.interfaces

// Defines several constants used when transmitting messages between the
// service and the UI.
internal interface MessageConstants {
    companion object {
        val MESSAGE_WRITE = 1
        val MESSAGE_TOAST = 2
        val MESSAGE_SWITCH_ACTIVITY = 3
        val MESSAGE_BLUETOOTH_CONNECTION_CLOSED = 4
        val MESSAGE_ERROR_BLUETOOTH_CONNECTION = 5
        val MESSAGE_BLUETOOTH_MESSAGE = 6
    }

}