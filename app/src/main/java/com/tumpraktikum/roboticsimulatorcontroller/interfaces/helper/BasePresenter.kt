package com.tumpraktikum.roboticsimulatorcontroller.interfaces.helper

/**
 * Created by patriccorletto on 12/3/17.
 */
interface BasePresenter<T> {
    fun takeView(view:T)
    fun dropView()
}