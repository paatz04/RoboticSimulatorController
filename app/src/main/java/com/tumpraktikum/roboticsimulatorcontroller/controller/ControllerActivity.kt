package com.tumpraktikum.roboticsimulatorcontroller.controller

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tumpraktikum.roboticsimulatorcontroller.R
import javax.inject.Inject

class ControllerActivity : AppCompatActivity() {

    @Inject lateinit var mPresenter: ControllerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)
        mPresenter.checkConnection()
    }
}
