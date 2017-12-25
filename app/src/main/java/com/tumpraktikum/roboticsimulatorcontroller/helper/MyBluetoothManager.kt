package com.tumpraktikum.roboticsimulatorcontroller.helper

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v7.app.AppCompatActivity


class MyBluetoothManager {

    private val mBluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    private var REQUEST_ENABLE_BT: Int = 1;

    fun isBluetoothSupported(): Boolean {
        return mBluetoothAdapter == null
    }

    fun isBluetoothEnabled(): Boolean {
        return mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled
    }

    fun enableBluetooth(context: AppCompatActivity) {
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(context, enableBtIntent, REQUEST_ENABLE_BT,null)
        }
    }

    fun queryPairedDevices(): Set<android.bluetooth.BluetoothDevice>? {
        if (mBluetoothAdapter != null) {
            return mBluetoothAdapter.bondedDevices
        }
        return null
    }


}
