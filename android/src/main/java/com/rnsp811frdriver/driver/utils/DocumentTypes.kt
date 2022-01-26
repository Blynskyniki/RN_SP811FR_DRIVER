package com.rnsp811frdriver.utils

import java.nio.charset.Charset

enum class DocumentTypes(val data: ByteArray) {
    NON_FISCAL_DOCUMENT("1".toByteArray(Charset.forName("UTF-8"))),
    SALE_DOCUMENT("2".toByteArray(Charset.forName("UTF-8"))),
    REFUND_DOCUMENT("3".toByteArray(Charset.forName("UTF-8"))),
    DEPOSITING_DOCUMENT("4".toByteArray(Charset.forName("UTF-8"))),
    WITCHDRAWAL_DOCUMENT("5".toByteArray(Charset.forName("UTF-8"))),
    OTHER("//".toByteArray(Charset.forName("UTF-8"))),

}
