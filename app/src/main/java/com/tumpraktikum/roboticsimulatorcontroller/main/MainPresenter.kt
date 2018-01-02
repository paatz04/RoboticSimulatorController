package com.tumpraktikum.roboticsimulatorcontroller.main

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import javax.inject.Inject


class MainPresenter @Inject constructor(private val myBluetoothManager: MyBluetoothManager) :
        MainContract.Presenter {

    private var mView: MainContract.View? = null

    override fun takeView(view: MainContract.View) {
        this.mView = view
        if (myBluetoothManager.isBluetoothSupported()) {
            checkIfBluetoothOn()
        } else {
            mView?.showNotSupported()
        }
    }

    override fun dropView() {
        this.mView = null
    }

    override fun checkIfBluetoothOn() {
        if (myBluetoothManager.isBluetoothEnabled()) {
            mView?.showBluetoothDevices()
        } else {
            mView?.showEmptyView()
        }
    }

    override fun turnBluetoothOn(context: AppCompatActivity) {
        myBluetoothManager.enableBluetooth(context)
    }

    override fun connectToDevice() {
    }

    override fun bluetoothDeviceFound(context: Context, intent: Intent) {
        val action = intent.action
        if (BluetoothDevice.ACTION_FOUND == action) {
            // Discovery has found a device. Get the BluetoothDevice
            // object and its info from the Intent.
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            val deviceName = device.name
            val deviceHardwareAddress = device.address // MAC address
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MyBluetoothManager.REQUEST_ENABLE_BT && resultCode == -1 && data != null) {
            // bluetooth turned on successfully
            mView?.showBluetoothDevices()
        }else
        {
            //something wen wrong with bluetooth intent
            mView?.showToast()
        }
    }
}