package com.rnsp811frdriver.utils


fun stringToASCII(data: String): ByteArray {
    return data.map { symbol ->
        return@map symbol.toByte()
    }.toByteArray()
}

fun toBinaryString(x: Int, len: Int): String {
    return String.format(
        "%" + len + "s",
        Integer.toBinaryString(x)
    ).replace(" ".toRegex(), "0")
}

fun checkBitInInt(n: Int, position: Int): Boolean {

    return n and (1 shl position) != 0

}
