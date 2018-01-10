package com.tumpraktikum.roboticsimulatorcontroller.controller.dataconverter

class TransferDataConverter {
    companion object {
        fun getStringForGrab(grabValue: Float): String{
            return "GRAB=" + grabValue
        }

        fun getStringForTip(tipValue: Float): String {
            return "TIP=" + tipValue
        }

        fun getStringForBody(bodyValue: Float): String {
            return "BODY=" + bodyValue
        }

        fun getStringForRotation(rotationValue: Float): String {
            return "ROTATION=" + rotationValue
        }
    }
}