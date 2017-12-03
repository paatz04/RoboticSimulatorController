package com.tumpraktikum.roboticsimulatorcontroller

/**
 * Created by patriccorletto on 12/3/17.
 */
interface MainContract {

    interface View : BaseView<Presenter> {
        fun updateView()
    }

    interface Presenter : BasePresenter {

    }
}