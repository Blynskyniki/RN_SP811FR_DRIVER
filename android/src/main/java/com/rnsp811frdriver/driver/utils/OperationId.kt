package com.rnsp811frdriver.utils

import android.util.Log

fun makeOperationId(): Byte {
    val id =     (21..80).random().toByte()
//    Log.d("SP811", "id: ${id}")

    return id

}
