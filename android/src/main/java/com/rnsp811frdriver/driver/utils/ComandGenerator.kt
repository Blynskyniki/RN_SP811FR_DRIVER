package com.rnsp811frdriver

import android.util.Log
import com.rnsp811frdriver.driver.utils.Utils
import com.rnsp811frdriver.utils.*
import com.rnsp811frdriver.utils.constants.Constants
import java.nio.ByteBuffer

class CommandGenerator(private val password:String = "PONE") {
    private var _command: ByteArray = byteArrayOf()

    // Без опций
    fun addCommand(code: Byte, data: ByteBuffer): CommandGenerator {


        this._command = this._command.plus(
            stringToASCII(
                String.format(
                    "%02x",
                    code
                )
            )
        )

        val byteArray = ByteArray(data.capacity())
        data.get(byteArray)
        this._command = this._command.plus(byteArray)
        this._command = this._command.plus(Constants.FS)
        this._command = this._command.plus(Constants.FS)


        return this
    }

    fun addCommand(code: Byte, data: ByteArray): CommandGenerator {


        this._command = this._command.plus(
            stringToASCII(
                String.format(
                    "%02x",
                    code
                )
            )
        )

        this._command = this._command.plus(data)
        return this
    }


    fun build(): ByteArray {
        var command = byteArrayOf()
        command = command.plus(Constants.PWD)
        val operationId = makeOperationId()

        command = command.plus(operationId)
        command = command.plus(this._command)
        command = command.plus(Constants.ETX)


        val crc = stringToASCII(
            String.format(
                "%02x",
                makeCRC(command)
            )
        )

        var full = byteArrayOf()
        full = full.plus(Constants.STX)
        full = full.plus(command)
        full = full.plus(crc)
        Log.d("SP811", "SEND COMMAND : ${Utils.getHexString(full)}")


        return full
    }

}
