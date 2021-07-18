package com.aceandtate.service

import com.aceandtate.model.FrameProductionSeed
import com.aceandtate.model.Supplier

enum ProductionInterval:
  case Regular(fromDay: Int, toDay: Int)
  case PreProduction(fromDay: Int, toDay: Int, productionDay: Int)

object FrameProductionSimulation:
  private def completedProductionWindows (seed: FrameProductionSeed, age: Int, days: Int): Int = 
    (days / (seed.windowBase + age * seed.ageFactor)).floor.toInt

  private def framesProduced(seed: FrameProductionSeed, age: Int, days: Int): Int = 
    completedProductionWindows(seed, age, days) * seed.framesPerWindow

  private def framesProducedAfterProductionDate(seed: FrameProductionSeed, age: Int, days:Int, daysToProduction:Int): Int =
    (framesProduced(seed, age, days) - framesProduced(seed, age, daysToProduction)).max(0)

  private def aggregateFrameProduction(supplier: Supplier, seed: FrameProductionSeed, frameProductors: Seq[ProductionInterval]): Int = 
    frameProductors.map {
      case ProductionInterval.Regular(fromDay, toDay) =>
        framesProduced(seed, supplier.age + fromDay, toDay - fromDay)
      case ProductionInterval.PreProduction(fromDay, toDay, productionDay) =>
        framesProducedAfterProductionDate(seed, supplier.age + fromDay, toDay - fromDay, productionDay - fromDay)
    }.sum

  private def lastDayOfProduction(seed: FrameProductionSeed, age: Int, days: Int): Int = 
    val completedWindows = completedProductionWindows(seed, age, days)
    completedWindows * seed.windowBase

  private def lastDayOfProductionAfterProductionDate(seed: FrameProductionSeed, age: Int, days: Int, daysToProduction: Int): Option[Int] = 
    Option(lastDayOfProduction(seed, age, days)).filter(_ >= daysToProduction)


  private def findLastDayOfProduction(supplier: Supplier, seed: FrameProductionSeed, frameProductors: Seq[ProductionInterval]): Int =
    frameProductors.map {
      case ProductionInterval.Regular(fromDay, toDay) =>
        fromDay + lastDayOfProduction(seed, supplier.age + fromDay, toDay - fromDay)
      case ProductionInterval.PreProduction(fromDay, toDay, productionDay) =>
        lastDayOfProductionAfterProductionDate(seed, supplier.age + fromDay, toDay - fromDay, productionDay - fromDay)
          .map(fromDay + _)
          .getOrElse(0)
    }.max

  private def createSimulationIntervals(days: Int, daysToProduction: Int): Seq[ProductionInterval] =
    (0 to days).map(day =>
      if day < daysToProduction then
        ProductionInterval.PreProduction(day, days, daysToProduction)
      else
        ProductionInterval.Regular(day, days)
    )

  def simulateProductionTotal(
    supplier: Supplier,
    seed: FrameProductionSeed,
    daysToProduction: Int,
    days: Int
  ): Int =
    val intervals = createSimulationIntervals(days, daysToProduction)
    aggregateFrameProduction(supplier, seed, intervals)

  def simulateProductionTotalSeq(
    suppliers: Seq[Supplier],
    seed: FrameProductionSeed,
    daysToProduction: Int,
    days: Int
  ): Int = 
    suppliers.map(simulateProductionTotal(_, seed, daysToProduction, days)).sum

  def simulateProductionLastDay(
    supplier: Supplier,
    seed: FrameProductionSeed,
    daysToProduction: Int,
    days: Int
  ): Int =
    val intervals = createSimulationIntervals(days, daysToProduction)
    findLastDayOfProduction(supplier, seed, intervals)
