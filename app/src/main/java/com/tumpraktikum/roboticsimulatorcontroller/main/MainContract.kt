package com.tumpraktikum.roboticsimulatorcontroller.main

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.BasePresenter
import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.BaseView


interface MainContract {

    interface View : BaseView<Presenter> {
        fun showEmptyView()
        fun showBluetoothDevices()
        fun showNotSupported()
        fun showLoading()
        fun openControllerActivity()
        fun setAdapter(): BluetoothListAdapter
        fun setPairedAdapter(): BluetoothListAdapter
        fun showToast(msg: String)
        fun setOtherListHeight()
        fun setPairedListHeight()
        fun showPermissionButton()

    }

    interface Presenter : BasePresenter<View> {
        fun locationPermissionGranted(granted: Boolean)
        fun turnBluetoothOn(context: AppCompatActivity)
        fun connectToDevice()
        fun bluetoothDeviceFound(context: Context, intent: Intent)
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
        fun cancelDiscovery()
        fun onItemClick(position: Int, pairedDevice: Boolean)
    }
}