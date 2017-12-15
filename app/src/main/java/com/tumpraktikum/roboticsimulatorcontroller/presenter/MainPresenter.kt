package com.tumpraktikum.roboticsimulatorcontroller.presenter

import com.tumpraktikum.roboticsimulatorcontroller.helper.MyBluetoothManager
import com.tumpraktikum.roboticsimulatorcontroller.interfaces.MainContract
import javax.inject.Inject



/**
 * Created by patriccorletto on 12/3/17.
 */
class MainPresenter: MainContract.Presenter {

    @Inject
    lateinit var mMyBluetoothManager: MyBluetoothManager


    fun MainPresenter(){
        
    }
    override fun takeView(view: MainContract.View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dropView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkIfBluetoothOn() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun turnBluetoothOn() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun connectToDevice() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}