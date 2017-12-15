package com.tumpraktikum.roboticsimulatorcontroller.main

import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.BasePresenter
import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.BaseView


interface MainContract {

    interface View : BaseView<Presenter> {
        fun showEmptyView()
        fun showBluetoothDevices()
        fun openControllerActivity()
    }

    interface Presenter : BasePresenter<View> {
        fun checkIfBluetoothOn()
        fun turnBluetoothOn()
        fun connectToDevice()
    }
}