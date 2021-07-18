package com.aceandtate.model

import io.circe.generic.auto._, io.circe.syntax._

case class Supplier (val name: String, val age: Int)


case class FrameProductionSeed(
  val ageFactor: Double = 0.01,
  val framesPerWindow: Int = 1000,
  val windowBase: Int = 8
)

case class GlassProductionSeed(
  val experienceFactor: Double = 0.03,
  val dailyProductionBase: Int = 50
)

case class Production (val glasses: Int, val frames: Int)

case class SuppliersSummaryContainer (val suppliers: List[SupplierTimeSummary])
case class SupplierTimeSummary (val name: String, val age_in_days: Int, val last_day_of_frame_production: Int)
