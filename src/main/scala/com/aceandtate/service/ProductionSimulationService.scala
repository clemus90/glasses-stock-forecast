package com.aceandtate.service

import com.aceandtate.model.GlassProductionSeed
import com.aceandtate.model.FrameProductionSeed
import com.aceandtate.model.Production
import com.aceandtate.model.Supplier
import com.aceandtate.model.SupplierTimeSummary
import com.aceandtate.repository.SupplierState
import com.aceandtate.service.GlassProductionSimulation
import com.aceandtate.service.FrameProductionSimulation


import zio._

trait ProductionSimulationService: 
  def simulateProductionTotals(days: Int): UIO[Production]
  def simulateSupplierSummaries(days: Int): UIO[List[SupplierTimeSummary]]

object ProductionSimulationService:
  def simulateProductionTotals(days: Int) = ZIO.serviceWith[ProductionSimulationService](_.simulateProductionTotals(days))
  def simulateSupplierSummaries(days: Int) = ZIO.serviceWith[ProductionSimulationService](_.simulateSupplierSummaries(days))

  val live = ZLayer.fromService[SupplierState, ProductionSimulationService](state => ProductionSimulationServiceFromState(state))

case class ProductionSimulationServiceFromState(state: SupplierState) extends ProductionSimulationService:
  private def suppliersFromMap(map: Map[String, Supplier]): List[Supplier] =
    map.toList.map({
      case (_, supplier) => supplier
    })

  override def simulateProductionTotals(days: Int): UIO[Production] =
    state.getState.map(stateMap =>
      val glasses = GlassProductionSimulation.simulateProductionTotalSeq(
        suppliersFromMap(stateMap),
        GlassProductionSeed(),
        30,
        days
      )
      
      val frames = FrameProductionSimulation.simulateProductionTotalSeq(
        suppliersFromMap(stateMap),
        FrameProductionSeed(),
        30,
        days
      )
      Production(glasses, frames) 
    )

  override def simulateSupplierSummaries(days: Int): UIO[List[SupplierTimeSummary]] =
    state.getState.map(stateMap =>
      suppliersFromMap(stateMap)
        .map( supplier =>
          val lastDay = FrameProductionSimulation.simulateProductionLastDay(
            supplier,
            FrameProductionSeed(),
            30,
            days
          )
          SupplierTimeSummary(supplier.copy(age = supplier.age + days), lastDay)
        )
    )

