package com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces

/**
 * Created by patriccorletto on 12/3/17.
 */
interface BasePresenter<T> {
    fun takeView(view:T)
    fun dropView()
}