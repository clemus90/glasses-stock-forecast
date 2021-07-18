package com.aceandtate

import com.aceandtate.service.ProductionSimulationService
import com.aceandtate.repository.SupplierState
import zhttp.http._
import zhttp.service.Server
import zio._

object StockForecastApp extends App:
  val app: HttpApp[Has[ProductionSimulationService], Nothing] = Http.collectM {
    case Method.GET -> Root / "forecast" / "stock" / days =>
      ProductionSimulationService.simulateProductionTotals(days.toInt).map(prod =>
          Response.text(s"result = $prod"))
    case Method.GET -> Root / "forecast" / "suppliers" / days =>
      ProductionSimulationService.simulateSupplierSummaries(days.toInt).map(summaries =>
          Response.text(s"result = $summaries"))
    case Method.POST -> Root / "suppliers" / "load" => ZIO.succeed(Response.http(status = Status.RESET_CONTENT))
  }

  val layers = SupplierState.live >>> ProductionSimulationService.live

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    Server.start(8080, app.silent)
      .provideLayer(layers)
      .exitCode
  
