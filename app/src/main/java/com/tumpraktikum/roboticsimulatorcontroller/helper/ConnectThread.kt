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





/**
 * Created by patriccorletto on 1/9/18.
 */
class ConnectThread(private val mmDevice: BluetoothDevice, private val mBluetoothAdapter: MyBluetoothManager, private val mHandler : Handler) : Thread() {

    private val MYUUID: UUID = UUID.fromString("04c6093b-0000-1000-8000-00805f9b34fb")
    private val mmSocket: BluetoothSocket?
    private var mBluetoothService: MyBluetoothService? = null

    init {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        var tmp: BluetoothSocket? = null
        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            tmp = mmDevice.createRfcommSocketToServiceRecord(MYUUID)
        } catch (e: IOException) {
            Log.e(TAG, "Socket's create() method failed", e)
        }

        mmSocket = tmp
    }

    override fun run() {
        // Cancel discovery because it otherwise slows down the connection.
        mBluetoothAdapter.cancelDiscovery()

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket!!.connect()
        } catch (connectException: IOException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket!!.close()
            } catch (closeException: IOException) {
                Log.e(TAG, "Could not close the client socket", closeException)
            }
            // Send a failure message back to the activity.
            val writeErrorMsg = mHandler.obtainMessage(MessageConstants.MESSAGE_TOAST)
            val bundle = Bundle()
            bundle.putString("toast",
                    "Error connecting: "+connectException.localizedMessage)
            writeErrorMsg.data = bundle
            mHandler.sendMessage(writeErrorMsg)
            return

        }
        mBluetoothService = MyBluetoothService(mHandler,mmSocket)
        mBluetoothAdapter.setService(mBluetoothService)

        // Send a failure message back to the activity.
        val msg = mHandler.obtainMessage(MessageConstants.MESSAGE_SWITCH_ACTIVITY)
        mHandler.sendMessage(msg)

    }

    // Closes the client socket and causes the thread to finish.
    fun cancel() {
        try {
            mmSocket!!.close()
        } catch (e: IOException) {
            Log.e(TAG, "Could not close the client socket", e)
        }
    }
}