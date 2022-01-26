package com.rnsp811frdriver.utils

import java.nio.charset.Charset


enum class Discount(val data: ByteArray) {
    PERCENT("%".toByteArray(Charset.forName("UTF-8"))),
    FOR_AMOUNT("s".toByteArray(Charset.forName("UTF-8"))),


}
