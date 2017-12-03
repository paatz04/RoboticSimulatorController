package com.tumpraktikum.roboticsimulatorcontroller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var bluetoothSelection: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnConnect.setOnClickListener { tryConnection() }
    }

    override fun onStart() {
        super.onStart()
        setViewState();
    }

    private fun tryConnection() {
        if (bluetoothSelection == null) {
            Toast.makeText(this, "Please select the bluetooth device you want to connect to first.", Toast.LENGTH_LONG).show()
            return
        }
        connect()
    }

    private fun connect() {
        var success = true;
        if (success)
            openControllerActivity()
    }

    private fun openControllerActivity() {
        val intent = Intent(this, ControllerActivity::class.java)
        startActivity(intent)
    }

    private fun setViewState() {
        var state = 0;
        when (state) {


        }
    }
}
