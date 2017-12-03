package com.tumpraktikum.roboticsimulatorcontroller

/**
 * Created by patriccorletto on 12/3/17.
 */
interface BaseView<T> {
    fun setPresenter(presenter: T)
}