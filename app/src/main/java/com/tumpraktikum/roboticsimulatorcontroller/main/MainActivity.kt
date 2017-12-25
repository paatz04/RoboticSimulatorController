package com.tumpraktikum.roboticsimulatorcontroller.main

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tumpraktikum.roboticsimulatorcontroller.R
import com.tumpraktikum.roboticsimulatorcontroller.application.App
import com.tumpraktikum.roboticsimulatorcontroller.controller.ControllerActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject




class MainActivity : AppCompatActivity(), MainContract.View {

    @Inject lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnConnect.setOnClickListener{ openControllerActivity() }

        (application as App).appComponent.inject(this)

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver, filter)
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val mReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            mPresenter.bluetoothDeviceFound(context,intent)
        }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.takeView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
    }

    override fun openControllerActivity() {
        val intent = Intent(this, ControllerActivity::class.java)
        startActivity(intent)
    }

    override fun showEmptyView() {

    }

    override fun showBluetoothDevices() {

    }

    override fun setAdapter() {
        listView.adapter = BluetoothListAdapter()
    }


}