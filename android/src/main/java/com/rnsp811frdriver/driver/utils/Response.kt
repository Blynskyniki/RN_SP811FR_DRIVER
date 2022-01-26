package com.rnsp811frdriver.utils

import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.rnsp811frdriver.driver.utils.Utils

class Response(val data: ByteArray) {
  private var errorCode: Int = 0
  var errorMsg: String? = ""
  val isError
    get() = errorCode > 0


  fun toWritableMap(): WritableMap {
    return Arguments.createMap().apply {
      this.putString("message", this@Response.errorMsg.orEmpty())
      this.putBoolean("isError", this@Response.isError)
    }
  }


  init {
    val code = byteArrayOf(data[4], data[5])
    this.errorCode = makeErrorByCodeInBytesInt(code)
//    Log.d("SP811", "==errorCode =======${this.errorCode}==================>")
    this.errorMsg = makeErrorByCodeInBytes(code)


//        Log.d("SP811", "===========================>")
//        Log.d("SP811", "Response ==> ${Utils.getHexString(data)} \nERROR_CODE ${Utils.getHexString(data[4])} ${Utils.getHexString(data[5])}")
//        Log.d("SP811", "STX ==> ${Utils.getHexString(data[0])}")
//        Log.d("SP811", "ID ==> ${Utils.getHexString(data[1])}")
//        Log.d("SP811", "COMMAND ==> ${Utils.getHexString(data[2])} ${Utils.getHexString(data[3])}")
//        Log.e("SP811", "errorCode ==> ${Utils.getHexString( byteArrayOf(data[4], data[5]))} }")
//        Log.d(
//            "SP811",
//            "DATA ==> ${Utils.getHexString(data.sliceArray(6..data.size-4))}"
//        )
////
//        Log.d("SP811", "===========================>")

  }

  override fun toString(): String {
    return this.errorMsg.orEmpty()
  }
}
