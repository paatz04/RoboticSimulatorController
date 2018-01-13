package com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces

/**
 * Created by patriccorletto on 1/9/18.
 */

// Defines several constants used when transmitting messages between the
// service and the UI.
internal interface MessageConstants {
    companion object {
        val MESSAGE_READ = 0
        val MESSAGE_WRITE = 1
        val MESSAGE_TOAST = 2
        val MESSAGE_SWITCH_ACTIVITY = 3
        val MESSAGE_BLUETOOTH_CONNECTION_CLOSED = 4
        val MESSAGE_ERROR_BLUETOOTH_CONNECTION = 5
    }

}