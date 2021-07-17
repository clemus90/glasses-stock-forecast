package com.aceandtate.repository

import com.aceandtate.model.Supplier
import zio._

trait SupplierState: 
  def getState: UIO[Map[String, Supplier]]
  def resetState(suppliers: List[Supplier]): UIO[Unit]

object SupplierState: 
  val live = ZLayer.succeed[SupplierState](InMemorySuppliersState())

case class InMemorySuppliersState() extends SupplierState:
  private var state: Map[String, Supplier] = Map()

  private def buildSuppliersMap(suppliers: List[Supplier]) =
    Map() ++ suppliers.map { supplier => supplier.name -> supplier }

  override def getState = UIO(state)

  override def resetState(suppliers: List[Supplier]): UIO[Unit] =
    UIO({
      state = buildSuppliersMap(suppliers)
      ()
    })
