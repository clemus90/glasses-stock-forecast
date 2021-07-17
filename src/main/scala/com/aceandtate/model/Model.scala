package com.aceandtate.model

case class Supplier (val name: String, val age: Int)

case class TemporalGlassesProduction (val amount: Int, val day: Int)
case class TemporalFramesProduction (val amount: Int, val day: Int)

case class Production (val glasses: Int, val frames: Int)

case class SupplierTimeSummary (val supplier: Supplier, lastDayOfFrameProduction: Int)

case class FrameProductionSeed(
  val ageFactor: Double = 0.01,
  val framesPerWindow: Int = 1000,
  val windowBase: Int = 8
)

case class GlassProductionSeed(
  val experienceFactor: Double = 0.03,
  val dailyProductionBase: Int = 50
)
