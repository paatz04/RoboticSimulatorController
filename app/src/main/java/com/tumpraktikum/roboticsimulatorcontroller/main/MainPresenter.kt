package com.tumpraktikum.roboticsimulatorcontroller.main

import android.util.Log
import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import javax.inject.Inject


/**
 * Created by patriccorletto on 12/3/17.
 */
class MainPresenter @Inject constructor(private val myBluetoothManager: MyBluetoothManager) : MainContract.Presenter {

    private var view: MainContract.View? = null;

    override fun takeView(view: MainContract.View) {
        this.view = view
    }

    override fun dropView() {
        this.view = null
    }

    override fun checkIfBluetoothOn() {
        Log.d("tag", myBluetoothManager.toString())
    }

    override fun turnBluetoothOn() {
    }

    override fun connectToDevice() {
    }
}