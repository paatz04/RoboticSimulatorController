package com.tumpraktikum.roboticsimulatorcontroller.controller

import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import javax.inject.Inject

class ControllerPresenter @Inject constructor(private val myBluetoothManager: MyBluetoothManager) : ControllerContract.Presenter {

    private var view: ControllerContract.View? = null;

    override fun takeView(view: ControllerContract.View) {
        this.view = view
    }

    override fun dropView() {
        this.view = null
    }

    override fun checkConnection() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}