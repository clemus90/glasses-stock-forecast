package com.aceandtate.repository

import com.aceandtate.model.Supplier

object SuppliersState:
  private var state: Map[String, Supplier] = Map()

  private def buildSuppliersMap(suppliers: List[Supplier]) =
    Map() ++ suppliers.map { supplier => supplier.name -> supplier }

  def resetState(suppliers: List[Supplier]): Unit =
    state = buildSuppliersMap(suppliers)
