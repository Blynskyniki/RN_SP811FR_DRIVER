package com.rnsp811frdriver.utils.constants

class Commands {
    companion object {
        const val PRINT_TXT = 0x21.toByte()
        const val INIT = 0x00.toByte()
        const val OPEN_DOCUMENT = 0x20.toByte()
        const val CLOSE_DOCUMENT = 0x22.toByte()
        const val ABORT_DOCUMENT = 0x23.toByte()
        const val PRINT_HEADER = 0x25.toByte()
        const val ADD_PRODUCT = 0x30.toByte()
        const val SUB_TOTAL = 0x34.toByte()
        const val X_REPORT = 0x60.toByte()
        const val Z_REPORT = 0x61.toByte()
        const val PRINT_CONTROL_TAPE = 0x50.toByte()
        const val SCROLL_CHECK_TAPE = 0xAC.toByte()
        const val SET_NDS = 0x37.toByte()
        const val OPEN_CASH_DRAWER = 0x80.toByte()
        const val CHECK_FR = 0xA0.toByte()
        const val PRINT_COPY_CHECK = 0x26.toByte()
        const val HOLD_DOCUMENT = 0x27.toByte()
        const val DISCOUNT = 0x32.toByte()
        const val GET_FR_PARAMS = 0xA1.toByte()
        const val SET_FR_PARAMS = 0xA2.toByte()
        const val CASH_OPERATION = 0x36.toByte()
        const val PAYMENT_OPERATION = 0x35.toByte()

    }
}
