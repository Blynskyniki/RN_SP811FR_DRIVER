package com.rnsp811frdriver.utils

import android.util.Log
import com.rnsp811frdriver.CommandGenerator
import com.rnsp811frdriver.driver.utils.Utils
import java.io.IOException
import java.lang.Exception
import java.net.InetSocketAddress
import java.net.Socket


class Transport(private val host: String, private val port: Int) {
    var client: Socket? = null

    companion object {
        val DEFAULT_BUFFER_SIZE = 65000
    }


    fun connect() {
      if(this.ping(this.host)){
        client = Socket()
        client?.connect(InetSocketAddress(this.host, this.port), 1000)
      }else {
        throw Exception("${this.host } недоступен")
      }

    }
  private fun ping(ip:String): Boolean {
    val runtime = Runtime.getRuntime()
    try {
      val mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 $ip")
      val mExitValue = mIpAddrProcess.waitFor()
      Log.d("SP811", "/system/bin/ping -c 1 $ip ===> $mExitValue ")
      return mExitValue == 0
    } catch (ignore: InterruptedException) {
      ignore.printStackTrace()
      Log.e("SP811", "ping  Exception:$ignore ")
      println(" Exception:$ignore")
    } catch (e: IOException) {
      e.printStackTrace()
      Log.e("SP811", "ping  Exception:$e ")

    }
    return false
  }

  fun sendCommandAndReceiveResponse(command: CommandGenerator): Response {
    client?.getOutputStream()?.write(command.build())
    val headMessage = ByteArray(DEFAULT_BUFFER_SIZE)
    val len = client?.getInputStream()?.read(headMessage)
    val res = Response(calculateArray(len!!, headMessage))
    return res
  }
  fun sendCommandAndReceiveResponse(command: ByteArray): ByteArray {
    client?.getOutputStream()?.write(command)
    val headMessage = ByteArray(DEFAULT_BUFFER_SIZE)
    val len = client?.getInputStream()?.read(headMessage)
    return calculateArray(len!!, headMessage)
  }

    fun closeConnection() {
        client?.close()

    }

    private fun calculateArray(len: Int, buff: ByteArray): ByteArray {
        val b = ByteArray(len)
        for (i in 0 until len) {
            b[i] = buff[i]
        }
        return b
    }
}
