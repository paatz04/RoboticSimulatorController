package com.tumpraktikum.roboticsimulatorcontroller.controller

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tumpraktikum.roboticsimulatorcontroller.R
import com.tumpraktikum.roboticsimulatorcontroller.application.App
import javax.inject.Inject

class ControllerActivity : AppCompatActivity(), ControllerContract.View {

    @Inject lateinit var mPresenter: ControllerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_controller)

        (application as App).appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        mPresenter.takeView(this)
    }

    override fun showEmptyView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
