package com.tumpraktikum.roboticsimulatorcontroller.controller
import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.*

interface ControllerContract {

    interface View : BaseView<Presenter> {
        fun showEmptyView()
    }

    interface Presenter : BasePresenter<View> {
        fun checkConnection()
    }
}