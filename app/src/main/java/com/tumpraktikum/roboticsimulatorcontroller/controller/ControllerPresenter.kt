package com.tumpraktikum.roboticsimulatorcontroller.controller

import android.util.Log
import com.tumpraktikum.roboticsimulatorcontroller.helper.MotionManager
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import javax.inject.Inject

class ControllerPresenter
@Inject constructor(private val myBluetoothManager: MyBluetoothManager,
                    private val motionManager: MotionManager)
    : ControllerContract.Presenter {

    private var view: ControllerContract.View? = null;

    override fun takeView(view: ControllerContract.View) {
        this.view = view
    }

    override fun dropView() {
        this.view = null
    }

    override fun checkConnection() {
        Log.d("tag", myBluetoothManager.toString())
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}