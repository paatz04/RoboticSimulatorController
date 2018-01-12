package com.tumpraktikum.roboticsimulatorcontroller.helper.interfaces

interface BasePresenter<T> {
    fun takeView(view:T)
}