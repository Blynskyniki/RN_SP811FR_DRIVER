package com.rnsp811frdriver.utils

fun makeCRC(bytes: ByteArray): Byte {
    var sum = 0
    for (i in 0..bytes.size - 1) {
        sum = sum xor (bytes[i].toInt() and 0xFF)
    }

    return (sum and 0xFF).toByte()
}

