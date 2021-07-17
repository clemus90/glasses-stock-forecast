package com.aceandtate.service

import com.aceandtate.model.GlassProductionSeed
import com.aceandtate.model.Supplier

object GlassProductionSimulation:
  private def dailyGlassProduction(age: Int, seed: GlassProductionSeed): Int =
    (seed.dailyProductionBase + age * seed.experienceFactor).floor.toInt

  def simulateProductionTotal(
    supplier: Supplier,
    seed: GlassProductionSeed,
    daysToProduction: Int,
    days: Int
  ): Int = 
    (0 to days).map(day =>
      if day > daysToProduction then dailyGlassProduction(supplier.age + day, seed) else 0
    ).sum

  def simulateProductionTotalSeq(
    suppliers: Seq[Supplier],
    seed: GlassProductionSeed,
    daysToProduction: Int,
    days: Int
  ): Int = 
    suppliers.map(simulateProductionTotal(_, seed, daysToProduction, days)).sum
    
