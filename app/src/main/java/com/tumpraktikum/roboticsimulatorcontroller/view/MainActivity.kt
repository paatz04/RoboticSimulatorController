package com.tumpraktikum.roboticsimulatorcontroller.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tumpraktikum.roboticsimulatorcontroller.R
import com.tumpraktikum.roboticsimulatorcontroller.interfaces.MainContract
import com.tumpraktikum.roboticsimulatorcontroller.presenter.MainPresenter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnConnect.setOnClickListener { mPresenter.checkIfBluetoothOn() }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.takeView(this)
    }

    override fun openControllerActivity() {
        val intent = Intent(this, ControllerActivity::class.java)
        startActivity(intent)
    }

    override fun showEmptyView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showBluetoothDevices() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}