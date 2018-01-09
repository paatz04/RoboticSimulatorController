package com.tumpraktikum.roboticsimulatorcontroller.helper

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.util.Log
import java.io.IOException
import java.util.*
import javax.inject.Inject


/**
 * Created by patriccorletto on 1/9/18.
 */
class ConnectThread(private val mmDevice: BluetoothDevice) : Thread() {

    private val MYUUID: UUID = UUID.fromString("ay571iutF91he2300leq")
    private val mmSocket: BluetoothSocket?

    @Inject lateinit var mBluetoothAdapter: MyBluetoothManager

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

            return
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        //manageMyConnectedSocket(mmSocket)
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