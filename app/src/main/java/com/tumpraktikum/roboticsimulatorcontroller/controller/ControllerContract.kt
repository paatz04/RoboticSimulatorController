package com.tumpraktikum.roboticsimulatorcontroller.controller
import com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces.*

interface ControllerContract {

    interface View : BaseView<Presenter> {
        fun showEmptyView()
    }

    interface Presenter : BasePresenter<View> {
        fun checkConnection()
        fun changeGrab(newValue: Double)
        fun changeTip(newValue: Double)
        fun changBody(newValue: Double)
        fun changeRotation(newValue: Double)
    }
}