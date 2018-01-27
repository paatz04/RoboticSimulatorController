package com.tumpraktikum.roboticsimulatorcontroller.interfaces

interface BasePresenter<T> {
    fun takeView(view:T)
}