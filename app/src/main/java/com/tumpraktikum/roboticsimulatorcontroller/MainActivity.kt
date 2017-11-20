package com.tumpraktikum.roboticsimulatorcontroller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clickListener = View.OnClickListener { view ->
            when (view.id) {
                R.id.btnConnect -> openControllerActivity()
            }
        }

        btnConnect.setOnClickListener(clickListener)
    }

    private fun openControllerActivity() {
        // Create an Intent to start the second activity
        val intent = Intent(this, ControllerActivity::class.java)

        // Start the new activity.
        startActivity(intent)
    }
}
