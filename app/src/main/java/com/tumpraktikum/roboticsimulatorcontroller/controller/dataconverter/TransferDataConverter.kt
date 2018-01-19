package com.tumpraktikum.roboticsimulatorcontroller.controller.dataconverter

class TransferDataConverter {
    companion object {
        private const val ANY_COLOR = 0
        private const val RED = 1
        private const val GREEN = 2
        private const val BLUE = 3

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

        fun getReceivedData(receivedMessage: String): ReceivedData {
            val receivedPartsString: List<String> = receivedMessage.split("=")
            if (receivedPartsString.size == 2)
                return ReceivedData(convertStrToRoboticSensorPart(receivedPartsString[0]), convertStrToInt(receivedPartsString[1]))
            else
                throw TransferDataConverterException("'$receivedMessage' isn't in correct format.")
        }

        private fun convertStrToRoboticSensorPart(strRoboticSensorPart: String): RoboticSensorPart {
            return when (strRoboticSensorPart) {
                "COLOR_GRAB" -> RoboticSensorPart.COLOR_GRAB
                "SCORE_RED" -> RoboticSensorPart.SCORE_RED
                "SCORE_BLUE" -> RoboticSensorPart.SCORE_BLUE
                "SCORE_GREEN" -> RoboticSensorPart.SCORE_GREEN
                "SCORE_MISSED" -> RoboticSensorPart.SCORE_MISSED
                else -> throw TransferDataConverterException("RoboticSensorPart not correct: '$strRoboticSensorPart'")
            }
        }

        private fun convertStrToInt(strInt: String): Int {
            try {
                return strInt.toInt()
            } catch (e: NumberFormatException) {
                throw TransferDataConverterException("'$strInt' isn't int.")
            } catch (e: IllegalAccessException) {
                throw TransferDataConverterException("'$strInt' isn't int.")
            }
        }

        fun getColor(color: Int): String {
            return when(color) {
                ANY_COLOR -> "-"
                RED -> "red"
                BLUE -> "blue"
                GREEN -> "green"
                else -> throw TransferDataConverterException("$color no color assigned.")
            }
        }
    }
}