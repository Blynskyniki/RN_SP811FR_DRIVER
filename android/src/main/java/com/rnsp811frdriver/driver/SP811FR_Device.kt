package com.rnsp811frdriver

import android.os.Build
import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableMap
import com.rnsp811frdriver.driver.utils.Utils
import com.rnsp811frdriver.utils.*
import com.rnsp811frdriver.utils.constants.Commands
import com.rnsp811frdriver.utils.constants.Constants
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class SP811FR_Device(private val transport: Transport, private val password: String) {
  private val charsetEncoder: Charset = Charset.forName("Cp866")

  //  Открыть доумент (продажа\возврат)
  fun openDocument(
    type: DocumentTypes,
    departmentNumber: Number = 1,
    operatorName: String = "Галя",
    documentNumber: Number = 1
  ): Response {
    var params = byteArrayOf()
//      doc type
    params += type.data
    params += Constants.FS
//      Номер отдела
    params += departmentNumber.toString().toByteArray()
    params += Constants.FS
//      Имя оператора
    params += charsetEncoder.encode(operatorName).toByteArray()
    params += Constants.FS
//      Номер документа
    params += documentNumber.toString().toByteArray()


    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.OPEN_DOCUMENT,
        params
      )
    )

    Log.d("SP811", "OPEN_DOCUMENT : " + res.errorMsg)

    return res
  }


  //  Статус смены
  fun getData(
    type: Number = 1,
  ): String {
    var params = byteArrayOf()

//      тип операции 1-21 (см доку)
    params += type.toString().toByteArray()

    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.GET_STATUS,
        params
      )
    )
    Log.d("SP811", "GET_STATUS : " + res.errorMsg)
    if (res.isError) {

      throw Exception(res.errorMsg)
    }
    Log.d("SP811", "GET_STATUS PAYLOAD: " + res.PAYLOAD)
    Log.d("SP811", "GET_STATUS PAYLOAD: " + res.clearedPayload.toString(Charsets.UTF_8))
    return res.clearedPayload.toString(Charsets.UTF_8)

  }

  // Закрыть доумент (распечатать чек)
  fun closeDocument(): Response {

    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.CLOSE_DOCUMENT,
        byteArrayOf()
      )
    )

    Log.d("SP811", "CLOSE_DOCUMENT : " + res.errorMsg)

    return res
  }

  // Печать заголовка для нового документа
  fun printHeader() {
    var params = byteArrayOf()
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.PRINT_HEADER,
        params
      )
    )
    Log.d("SP811", "PRINT_HEADER : " + res.errorMsg)

  }

  private fun getByteArrayImage(imageUrl: String): ByteArray? {
    var url: URL? = null
    try {
      url = URL(imageUrl)
    } catch (e: MalformedURLException) {
      e.printStackTrace()
    }
    val outputStream = ByteArrayOutputStream()
    try {
      val chunk = ByteArray(4096)
      var bytesRead: Int
      val stream = url!!.openStream()
      while (stream.read(chunk).also { bytesRead = it } > 0) {
        outputStream.write(chunk, 0, bytesRead)
      }
      url.openStream().close()
    } catch (e: IOException) {
      e.printStackTrace()
      return null
    }
    return outputStream.toByteArray()
  }


  // Печать заголовка для нового документа
  fun setHeader(url: String): Response? {
    this.getByteArrayImage(url)?.let { image ->
      Log.d("SP811", "setHeader size : ${image.size}")
      var size = image.size
      if (size > 3700) {
        throw Exception("Размер логотипа должен быть не более 3,6 кб. Размер 288x100")
      }
      var res = requestWrapper(
        CommandGenerator(this.password).addCommand(
          Commands.SET_HEADER,
          size.toString().toByteArray(),

          ).build()
      )
      Log.d("SP811", "Constants.ACK =  : ${Utils.getHexString(res)}")
      if (res[0] == Constants.ACK) {

        var imageData = byteArrayOf(0x1B)
        imageData += image

        val res2 = requestWrapper(
          imageData
        )

        Log.d("SP811", "setHeader  data: " + Response(res2).errorMsg)

        return Response(res2)

      } else {
        Log.d("SP811", "setHeader  data: " + Response(res).errorMsg)
        return Response(res)
      }

    }
    return null
  }

  // Удалить заголовка для нового документа
  fun rmHeader(): Response {
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.RM_HEADER,
        byteArrayOf()
      )
    )
    Log.d("SP811", "RM_HEADER : " + res.errorMsg)

    return res
  }

  // Аннулировать документ
  fun abortDocument(): Response {

    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.ABORT_DOCUMENT,
        byteArrayOf()
      )
    )

    Log.d("SP811", "ABORT_DOCUMENT : " + res.errorMsg)

    return res

  }

  // Верхнее клише
  fun setHeaderTxt(data: ReadableArray) {
//     Зануляем заголовок
    this.setFrParams(20, 0, "")
    this.setFrParams(20, 1, "")
    this.setFrParams(20, 2, "")
    this.setFrParams(20, 3, "")

    for (i in 0 until data.size()) {
      val res =
        this.setFrParams(20, i, charsetEncoder.encode(data.getString(i).orEmpty()).toByteArray())
      Log.d("SP811", "SET_HEADER_TXT $i : " + res.errorMsg)
    }

  }

  // Нижнее клише
  fun setFooterTxt(data: ReadableArray) {
//     Зануляем заголовок
    this.setFrParams(21, 0, "")
    this.setFrParams(21, 1, "")

    for (i in 0 until data.size()) {
      val res =
        this.setFrParams(20, i, charsetEncoder.encode(data.getString(i).orEmpty()).toByteArray())
      Log.d("SP811", "SET_FOOTER_TXT $i : " + res.errorMsg)
    }

  }

  // Подитог
  fun subTotal(): Response {

    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.SUB_TOTAL,
        byteArrayOf()
      )
    )

    Log.d("SP811", "SUB_TOTAL : " + res.errorMsg)

    return res
  }

  // Скидка на товар/чек
  fun saleForDocOrProduct(type: Discount, percentOrSum: Int, name: String = ""): Response {
    var params = byteArrayOf()
//      doc type
    params += type.data
    params += Constants.FS
    params += charsetEncoder.encode(name).toByteArray()
    params += Constants.FS
    params += percentOrSum.toString().toByteArray()
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.DISCOUNT,
        params
      )
    )

    Log.d("SP811", "DISCOUNT : " + res.errorMsg)

    return res
  }

  // Распечатать отчет без гашения (X-отчет)
  fun xReport(): Response {
    var params = byteArrayOf()
    params = params.plus("01".toByteArray())
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.X_REPORT,
        params
      )
    )

    Log.d("SP811", "X_REPORT : " + res.errorMsg)

    return res
  }

  // Распечатать отчет с гашением (Z-отчет)
  fun zReport(): Response {
    var params = byteArrayOf()
    params += "01".toByteArray()
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.Z_REPORT,
        params
      )
    )

    Log.d("SP811", "Z_REPORT : " + res.errorMsg)

    return res
  }

  // Инициализация ФР (Перед любым чеком)
  fun initFR(): Response {
    val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      LocalDateTime.now()
    } else {
      TODO("VERSION.SDK_INT < O")
    }

    val formattedDate = date.format(DateTimeFormatter.ofPattern("DDMMYY"))
    val formattedHour = date.format(DateTimeFormatter.ofPattern("HHmmss"))

    var params = formattedDate.toByteArray(Charset.defaultCharset());
    params = params.plus(Constants.FS)
    params = params.plus(formattedHour.toByteArray(Charset.defaultCharset()))
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.INIT,
        params
      )
    )

    Log.d("SP811", "initFR : " + res.errorMsg)
    return res

  }

  // Открыть денежный ящик
  fun openCashDrawer(): Response {
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.OPEN_CASH_DRAWER,
        byteArrayOf()
      )
    )

    Log.d("SP811", "OPEN_CASH_DRAWER : " + res.errorMsg)

    return res

  }

  // Установить ставки Ндс
  fun setNDS(): Response {

    var params = byteArrayOf()
    params = params.plus("0".toByteArray())
    params = params.plus(Constants.FS)
    params = params.plus("20".toByteArray())
    params = params.plus(Constants.FS)
    params = params.plus("1".toByteArray())
    params = params.plus(Constants.FS)
    params = params.plus("10".toByteArray())


    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.SET_NDS,
        params
      )
    )

    Log.d("SP811", "setNDS : " + res.errorMsg)

    return res

  }

  // печать произвольного текста
  fun printText(data: String): Response {
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.PRINT_TXT,
        charsetEncoder.encode(data)
      )
    )

    Log.d("SP811", res.toString())
    return res
  }

  // отложить чек
  // данные удаляются из оперативной памяти ФР и печатается причина отказа от чека.
  fun holdCheck(): Response {
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.HOLD_DOCUMENT,
        charsetEncoder.encode("").toByteArray()
      )
    )

    Log.d("SP811", "HOLD_DOCUMENT $res")
    return res
  }

  // печать Копия чека
  fun printCopyCheck(): Response {
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.PRINT_COPY_CHECK,
        byteArrayOf()
      )
    )

    Log.d("SP811", "PRINT_COPY_CHECK $res")
    return res
  }

  // Добавить товарную позицию
  fun addProduct(product: Product): Response {
    val q = ByteBuffer.allocate(2)
    q.toByteArray()
    var params = byteArrayOf()
//          Название товара
    params += charsetEncoder.encode(product.name).toByteArray()
    params += Constants.FS
//           Артикул
    params += charsetEncoder.encode(product.articul).toByteArray()
    params += Constants.FS
//            Количество
    params += product.count.toString().toByteArray()
    params += Constants.FS
//           цена
    Log.d("SP811", "ADD_PRODUCT PRICE  : ${product.price.toString()}")

    params += product.price.toString().toByteArray()
    params += Constants.FS
//          товарной группы
    params += "0".toByteArray()
    params += Constants.FS
    // налог идентификатор
    params += product.nds.toString().toByteArray()
    params += Constants.FS
//            Код товара GTIN
    params += product.gtin.toByteArray()
    params += Constants.FS
    //Тип кода GTIN
    //В этом поле передается тип кода
    //GTIN. В случае пустой строки, ФР
    //посылает в СКНО признак EAN (при
    //наличии Кода товара GTIN)
    params += product.gtinType.toByteArray()


    val res = requestWrapper(

      CommandGenerator(this.password).addCommand(
        Commands.ADD_PRODUCT,
        params
      )
    )

    Log.d("SP811", "ADD_PRODUCT  : ${res.errorMsg}")

    return res

  }

  // Запрос статуса состояния ФР
  fun checkFr(): WritableMap? {

    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.CHECK_FR,
        byteArrayOf()
      )
    )
    val fatal = String(byteArrayOf(res.data[6])).toInt()
    val current = String(byteArrayOf(res.data[8])).toInt()
    val doc = String(byteArrayOf(res.data[10])).toInt()
    SP_811_FR_CURRENT_ERRORS
    Log.d(
      "SP811",
      "fatal ==> ${getFrFatalErrors(fatal, SP_811_FR_FATAL_ERRORS)}"
    )
    Log.d(
      "SP811",
      "current ==> ${getFrFatalErrors(current, SP_811_FR_CURRENT_ERRORS)}"
    )
//    val first = (doc and 0xF0) shr 4
    val second = doc and 0x0F

    Log.d(
      "SP811",
      "doc ==> ${SP_811_FR_DOC_ERRORS[second]}"

    )
    val data = Arguments.createMap()
    data.putString("fatal", getFrFatalErrors(fatal, SP_811_FR_FATAL_ERRORS).toString())
    data.putString("current", getFrFatalErrors(current, SP_811_FR_CURRENT_ERRORS).toString())
    data.putString("doc", SP_811_FR_DOC_ERRORS[second].toString())

    Log.d("SP811", "CHECK_FR : " + res.toString() + "\n" + Utils.getHexString(res.data))

    return data
  }

  fun getFrParams(row: Int, column: Int): String {
    var params = byteArrayOf()
    params += row.toString().toByteArray()
    params += Constants.FS
    params += column.toString().toByteArray()
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.GET_FR_PARAMS,
        params
      )
    )
    Log.d("SP811", "GET_FR_PARAMS ${res.errorMsg}")
    Log.d("SP811", "GET_FR_PARAMS ${Utils.getHexString(res.data)}")

    val doc = String(byteArrayOf(res.data[6])).toInt()


    return "$doc";

  }

  fun setFrParams(row: Int, column: Int, value: String): Response {
    var params = byteArrayOf()
    params += row.toString().toByteArray()
    params += Constants.FS
    params += column.toString().toByteArray()
    params += Constants.FS
    params += value.toByteArray()
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.SET_FR_PARAMS,
        params
      )
    )
    Log.d("SP811", "SET_FR_PARAMS  ${res.errorMsg}")

    return res
  }

  fun setFrParams(row: Int, column: Int, value: ByteArray): Response {
    var params = byteArrayOf()
    params += row.toString().toByteArray()
    params += Constants.FS
    params += column.toString().toByteArray()
    params += Constants.FS
    params += value
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.SET_FR_PARAMS,
        params
      )
    )
    Log.d("SP811", "SET_FR_PARAMS  ${res.errorMsg}")

    return res
  }

  fun cashInOutOperation(cashName: String, summOrCount: Int): Response {
    var params = byteArrayOf()

    params += charsetEncoder.encode(cashName).toByteArray()
    params += Constants.FS
    params += summOrCount.toString().toByteArray()
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.CASH_OPERATION,
        params
      )
    )
    Log.d("SP811", "CASH_OPERATION  ${res.errorMsg}")

    return res
  }

  fun payment(type: Int, summOrCount: Int, text: String): Response {
    var params = byteArrayOf()
    params += type.toString().toByteArray()
    params += Constants.FS
    params += summOrCount.toString().toByteArray()
    params += Constants.FS
    params += charsetEncoder.encode(text).toByteArray()
    val res = requestWrapper(
      CommandGenerator(this.password).addCommand(
        Commands.PAYMENT_OPERATION,
        params
      )
    )
    Log.d("SP811", "PAYMENT_OPERATION  ${res.errorMsg}")
    return res
  }


  private fun requestWrapper(command: CommandGenerator): Response {
    return this.transport.sendCommandAndReceiveResponse(command)
  }

  private fun requestWrapper(command: ByteArray): ByteArray {
    return this.transport.sendCommandAndReceiveResponse(command)
  }

}

fun ByteBuffer.toByteArray(): ByteArray {
  val byteArray = ByteArray(this.capacity())
  this.get(byteArray)
  return byteArray
}
