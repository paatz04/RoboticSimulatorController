package com.tumpraktikum.roboticsimulatorcontroller.controller.transfer

class TransferDataFormatter {
    companion object {
        public fun getStringForGrab(grabValue: Float): String{
            return "GRAB=" + grabValue
        }

        public fun getStringForTip(tipValue: Float): String {
            return "TIP=" + tipValue
        }

        public fun getStringForBody(bodyValue: Float): String {
            return "BODY=" + bodyValue
        }

        public fun getStringForRotation(rotationValue: Float): String {
            return "ROTATION=" + rotationValue
        }


    }
}