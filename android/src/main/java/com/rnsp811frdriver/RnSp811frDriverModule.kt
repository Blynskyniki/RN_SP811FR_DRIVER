package com.rnsp811frdriver

import android.util.Log
import com.facebook.react.bridge.*
import com.rnsp811frdriver.utils.*

class RnSp811frDriverModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {
  private var driver: SP811FR_Device? = null;
  private var transport: Transport? = null;
  override fun getName(): String {
    return "RnSp811frDriver"
  }

  //Arguments.createMap()
  @ReactMethod
  fun connect(
    data: ReadableMap,
    promise: Promise
  ) {
    val host = data.getString("host");
    val port = data.getInt("port");
    val password = data.getString("password");

    if (!host.isNullOrEmpty() && port > 0 && !password.isNullOrEmpty()) {
      this.transport = Transport(host, port)
      this.transport?.let {
        it.connect()
        this@RnSp811frDriverModule.driver = SP811FR_Device(this.transport!!, password)
      }

      promise.resolve(Arguments.createMap())
    } else {
      promise.reject(Exception("Не переданы обязательные параметры для подключения "))
    }

  }

  @ReactMethod
  fun disconnect(
    promise: Promise
  ) {


    this.transport?.let {
      it.closeConnection()
      this@RnSp811frDriverModule.driver = null
    }

    promise.resolve(Arguments.createMap())


  }

  @ReactMethod
  fun openDocument(
    type: String,
    promise: Promise
  ) {
//    this.useNewTread(promise) {
    var res: Response? = null
    when (type) {
      "1" -> res = this.driver?.openDocument(DocumentTypes.NON_FISCAL_DOCUMENT)
      "2" -> res = this.driver?.openDocument(DocumentTypes.SALE_DOCUMENT)
      "3" -> res = this.driver?.openDocument(DocumentTypes.REFUND_DOCUMENT)
      "4" -> res = this.driver?.openDocument(DocumentTypes.DEPOSITING_DOCUMENT)
      "5" -> res = this.driver?.openDocument(DocumentTypes.WITCHDRAWAL_DOCUMENT)
      "6" -> promise.resolve(this.driver?.openDocument(DocumentTypes.OTHER))

      else -> promise.reject(Exception("Тип документа $type не существует"))
    }

    if (res?.isError == true) {
      promise.reject(java.lang.Exception(res.errorMsg))
    } else {
      promise.resolve(Arguments.createMap())
    }

//    }


  }

  @ReactMethod
  fun initFR(
    promise: Promise
  ) {
    this.useNewTread(promise) {
      val res = this.driver?.initFR()
      if (res?.isError == true) {
        promise.reject(java.lang.Exception(res.errorMsg))
      } else {
        promise.resolve(Arguments.createMap())
      }
    }


  }

  @ReactMethod
  fun addProduct(
    data: ReadableMap,
    promise: Promise
  ) {

    this.useNewTread(promise) {
      val res = this.driver?.addProduct(Product.fromReadableMap(data))
      if (res?.isError == true) {
        promise.reject(java.lang.Exception(res.errorMsg))
      } else {
        promise.resolve(Arguments.createMap())
      }
    }


  }
  @ReactMethod
  fun payment(
    data: ReadableMap,
    promise: Promise
  ) {
    val type = data.getInt("type")
    val sum = data.getInt("sum")
    val text = data.getString("text")!!
    this.useNewTread(promise) {
      val res = this.driver?.payment(type,sum,text)
      if (res?.isError == true) {
        promise.reject(java.lang.Exception(res.errorMsg))
      } else {
        promise.resolve(Arguments.createMap())
      }
    }


  }

  @ReactMethod
  fun saleForDocOrProduct(
    data: ReadableMap,
    promise: Promise
  ) {
    val type = data.getString("type")!!
    val percentOrSum = data.getInt("percentOrSum")
    val name = data.getString("name")!!

    var res: Response? = null
    this.useNewTread(promise) {
      when (type) {
        "p" -> res =
          this.driver?.saleForDocOrProduct(Discount.FOR_AMOUNT, percentOrSum, name)
        "s" -> res =
          this.driver?.saleForDocOrProduct(Discount.PERCENT, percentOrSum, name)
        else -> promise.reject(java.lang.Exception("Нет такого типа скидки"))
      }

      if (res?.isError == true) {
        promise.reject(java.lang.Exception(res?.errorMsg))
      } else {
        promise.resolve(Arguments.createMap())
      }
    }


  }

  @ReactMethod
  fun openCashDrawer(
    promise: Promise
  ) {
    this.useNewTread(promise) {
      val res = this.driver?.openCashDrawer()
      if (res?.isError == true) {
        promise.reject(java.lang.Exception(res.errorMsg))
      } else {
        promise.resolve(Arguments.createMap())
      }
    }


  }

  @ReactMethod
  fun checkFr(
    promise: Promise
  ) {
    this.useNewTread(promise) {
      promise.resolve(this.driver?.checkFr())
    }


  }

  @ReactMethod
  fun printText(
    data: String,
    promise: Promise
  ) {
    this.useNewTread(promise) {
      val res = this.driver?.printText(data)
      if (res?.isError == true) {
        promise.reject(java.lang.Exception(res.errorMsg.orEmpty()))
      } else {
        promise.resolve(Arguments.createMap())
      }
    }


  }

  @ReactMethod
  fun abortDocument(
    promise: Promise
  ) {
    this.useNewTread(promise) {
      val res = this.driver?.abortDocument()
      if (res?.isError == true) {
        promise.reject(java.lang.Exception(res.errorMsg))
      } else {
        promise.resolve(Arguments.createMap())
      }
    }


  }

  @ReactMethod
  fun holdCheck(
    promise: Promise
  ) {
    this.useNewTread(promise) {
      val res = this.driver?.holdCheck()
      if (res?.isError == true) {
        promise.reject(java.lang.Exception(res.errorMsg))
      } else {
        promise.resolve(Arguments.createMap())
      }
    }


  }

  @ReactMethod
  fun printCopyCheck(
    promise: Promise
  ) {
    this.useNewTread(promise) {
      val res = this.driver?.printCopyCheck()
      if (res?.isError == true) {
        promise.reject(java.lang.Exception(res.errorMsg))
      } else {
        promise.resolve(Arguments.createMap())
      }
    }


  }

  @ReactMethod
  fun xReport(
    promise: Promise
  ) {
    this.useNewTread(promise) {
      val res = this.driver?.xReport()
      if (res?.isError == true) {
        promise.reject(java.lang.Exception(res.errorMsg))
      } else {
        promise.resolve(Arguments.createMap())
      }
    }


  }

  @ReactMethod
  fun zReport(
    promise: Promise
  ) {
    this.useNewTread(promise) {
      val res = this.driver?.zReport()
      if (res?.isError == true) {
        promise.reject(java.lang.Exception(res.errorMsg))
      } else {
        promise.resolve(Arguments.createMap())
      }
    }


  }

  @ReactMethod
  fun subTotal(
    promise: Promise
  ) {
    this.useNewTread(promise) {
      val res = this.driver?.subTotal()
      if (res?.isError == true) {
        promise.reject(java.lang.Exception(res.errorMsg))
      } else {
        promise.resolve(Arguments.createMap())
      }
    }


  }

  @ReactMethod
  fun setNDS(
    promise: Promise
  ) {
    this.useNewTread(promise) {
      val res = this.driver?.setNDS()
      if (res?.isError == true) {
        promise.reject(java.lang.Exception(res.errorMsg))
      } else {
        promise.resolve(Arguments.createMap())
      }
    }


  }

  @ReactMethod
  fun closeDocument(
    promise: Promise
  ) {
    this.useNewTread(promise) {
      val res = this.driver?.closeDocument()
      if (res?.isError == true) {
        promise.reject(java.lang.Exception(res.errorMsg))
      } else {
        promise.resolve(Arguments.createMap())
      }
    }


  }

  private fun useNewTread(promise: Promise, job: () -> Unit) {
    val id = Thread {
      try {

        job()

      } catch (e: java.lang.Exception) {
        e.message?.let { Log.e("SP811", it) }
        promise.reject(e)
      }
    }
    id.start()
  }


}
