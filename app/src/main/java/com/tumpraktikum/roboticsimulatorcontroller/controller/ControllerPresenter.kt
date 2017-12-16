package com.tumpraktikum.roboticsimulatorcontroller.controller

import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import javax.inject.Inject

class ControllerPresenter
@Inject constructor(private val myBluetoothManager: MyBluetoothManager)
    : ControllerContract.Presenter {

    private var mView: ControllerContract.View? = null;


    override fun takeView(view: ControllerContract.View) {
        mView = view
    }

    override fun dropView() {
        mView = null
    }

    override fun checkConnection() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeGrab(newValue: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeTip(newValue: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changBody(newValue: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeRotation(newValue: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}