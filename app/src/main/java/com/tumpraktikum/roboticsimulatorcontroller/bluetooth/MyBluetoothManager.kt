package com.tumpraktikum.roboticsimulatorcontroller.bluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.app.AppCompatActivity
import android.util.Log

/*
The BluetoothManager Class helps to work with the android bluetooth library. It provides functionality
to check if bluetooth is available, turned on or off, start and cancel discovery and also to to turn
bluetooth on and off from within the application
 */
class MyBluetoothManager {
    companion object {
        const val REQUEST_ENABLE_BT: Int = 1
    }

    private val mBluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    //holds a reference to the established MyBluetoothService connection
    private lateinit var mBluetoothService: MyBluetoothService

    fun isBluetoothEnabled(): Boolean {
        return mBluetoothAdapter.isEnabled
    }

    fun enableBluetooth(context: AppCompatActivity) {
        if (!isBluetoothEnabled()) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(context, enableBtIntent, REQUEST_ENABLE_BT, null)
        }
    }

    fun setService(myBluetoothService: MyBluetoothService) {
        Log.d(MyBluetoothManager::class.toString(), "setService()")
        if (::mBluetoothService.isInitialized)
            mBluetoothService.close()
        mBluetoothService = myBluetoothService
    }

    fun getService(): MyBluetoothService {
        return mBluetoothService
    }

    fun startDiscovery() {
        mBluetoothAdapter.startDiscovery()
    }

    fun cancelDiscovery() {
        mBluetoothAdapter.cancelDiscovery()
    }

    fun queryPairedDevices(): Set<android.bluetooth.BluetoothDevice> {
        return mBluetoothAdapter.bondedDevices
    }
}
