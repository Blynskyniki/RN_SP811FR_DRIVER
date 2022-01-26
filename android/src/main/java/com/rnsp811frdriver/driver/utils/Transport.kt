package com.rnsp811frdriver.utils

import com.rnsp811frdriver.CommandGenerator
import java.net.InetSocketAddress
import java.net.Socket

class Transport(private val host: String, private val port: Int) {
    var client: Socket? = null

    companion object {
        val DEFAULT_BUFFER_SIZE = 65000
    }


    fun connect() {
        client = Socket()
        client?.connect(InetSocketAddress(this.host, this.port), 1000)
    }

    fun sendCommandAndReceiveResponse(command: CommandGenerator): Response {
        client?.getOutputStream()?.write(command.build())
        val headMessage = ByteArray(DEFAULT_BUFFER_SIZE)
        val len = client?.getInputStream()?.read(headMessage)
        val res = Response(calculateArray(len!!, headMessage))
        return res
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
