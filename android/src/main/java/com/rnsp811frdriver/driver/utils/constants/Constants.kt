package com.rnsp811frdriver.utils.constants

import java.nio.charset.Charset

class Constants {
    companion object {
        val STX = 0x02.toByte()
        val ETX = 0x03.toByte()
        val FS = 0x1C.toByte()
        val MIN_ID_DECIMAL = 20
        val MAX_ID_DECIMAL = 253
        val PWD = "PONE".toByteArray(Charset.forName("UTF-8"))
    }
}
