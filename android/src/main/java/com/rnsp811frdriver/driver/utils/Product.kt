package com.rnsp811frdriver.utils

import com.facebook.react.bridge.ReadableMap
import java.nio.charset.Charset

class Product(
  val articul: String,
  val nds: Number,
  val price: Double,
  val name: String,
  val count: Double,
  val gtin: String,
  val gtinType: String = "",


  ) {
  companion object {
    fun fromReadableMap(data: ReadableMap): Product {
      val articul = data.getString("articul")!!
      val nds = data.getInt("nds")
      val price = data.getDouble("price")
      val name = data.getString("name")!!
      val count = data.getDouble("count")
      val gtin = data.getString("gtin")!!
      return Product(articul, nds, price, name, count, gtin)
    }

  }
}
