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
        fun openControllerActivity()
        fun setAdapter()
    }

    interface Presenter : BasePresenter<View> {
        fun checkIfBluetoothOn()
        fun turnBluetoothOn(context: AppCompatActivity)
        fun connectToDevice()
        fun bluetoothDeviceFound(context: Context, intent: Intent)
    }
}