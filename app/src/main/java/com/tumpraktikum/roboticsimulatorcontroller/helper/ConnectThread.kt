package com.tumpraktikum.roboticsimulatorcontroller.helper

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.MessageConstants
import java.io.IOException
import java.util.*

class ConnectThread(private val mDevice: BluetoothDevice, private val mBluetoothManager: MyBluetoothManager, private val mHandler : Handler) : Thread() {

    private val MYUUID: UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb")
    private val mSocket: BluetoothSocket
    private lateinit var mBluetoothService: MyBluetoothService

    init {
         mSocket = getBluetoothSocket()
    }

    @Throws(ConnectThreadException::class)
    private fun getBluetoothSocket() : BluetoothSocket {
        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            return mDevice.createRfcommSocketToServiceRecord(MYUUID)
        } catch (e: IOException) {
            Log.e(TAG, "Socket's create() method failed", e)
            throw ConnectThreadException("Socket's create() method failed")
        }
    }

    override fun run() {
        // Cancel discovery because it otherwise slows down the connection.
        mBluetoothManager.cancelDiscovery()

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mSocket.connect()
        } catch (connectException: IOException) {
            closeBluetoothSocket()
            mBluetoothManager.startDiscovery()
            sendFailureMessageBackToActivity(connectException.localizedMessage)
            return
        }
        mBluetoothService = MyBluetoothService(mHandler, mSocket)
        mBluetoothManager.setService(mBluetoothService)

        sendSwitchActivityMessageBackToActivity()
    }

    private fun closeBluetoothSocket() {
        try {
            mSocket.close()
        } catch (closeException: IOException) {
            Log.e(TAG, "Could not close the client socket", closeException)
        }
    }

    private fun sendFailureMessageBackToActivity(message: String) {
        val writeErrorMsg = mHandler.obtainMessage(MessageConstants.MESSAGE_TOAST)
        val bundle = Bundle()
        bundle.putString("toast", "Error connecting: " + message)
        writeErrorMsg.data = bundle
        mHandler.sendMessage(writeErrorMsg)
    }

    private fun sendSwitchActivityMessageBackToActivity() {
        val msg = mHandler.obtainMessage(MessageConstants.MESSAGE_SWITCH_ACTIVITY)
        mHandler.sendMessage(msg)
    }

    // Closes the client socket and causes the thread to finish.
    fun cancel() {
        try {
            mSocket.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the client socket", e)
        }
    }
}