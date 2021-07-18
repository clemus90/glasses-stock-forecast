package com.aceandtate.adapter

import com.aceandtate.model.Supplier
import scala.xml.XML
import scala.xml.Elem

object SupplierPayload:
  private def querySuppliers(doc: Elem) =
    (for
      suppliers <- doc \ "suppliers"
      supplier <- suppliers \ "supplier"
    yield
      val name = supplier \@ "name"
      val age = (supplier \@ "age").toInt
      Supplier(name, age)).toList

  def fromString(xmlPayload: String) =
    val doc = XML.loadString(xmlPayload)
    querySuppliers(doc)
