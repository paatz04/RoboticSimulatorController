package com.tumpraktikum.roboticsimulatorcontroller.interfaces

import com.tumpraktikum.roboticsimulatorcontroller.interfaces.helper.BasePresenter
import com.tumpraktikum.roboticsimulatorcontroller.interfaces.helper.BaseView

/**
 * Created by patriccorletto on 12/3/17.
 */
interface MainContract {

    interface View : BaseView<Presenter> {
        fun showEmptyView()
        fun showBluetoothDevices()
    }

    interface Presenter : BasePresenter {
        fun checkIfBluetoothOn()
        fun turnBluetoothOn()
        fun connectToDevice()
    }
}