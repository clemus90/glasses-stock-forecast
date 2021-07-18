package com.aceandtate

import com.aceandtate.service.ProductionSimulationService
import com.aceandtate.repository.SupplierState
import com.aceandtate.adapter.SupplierPayload

import zhttp.http._
import zhttp.service.Server
import zio._

object StockForecastApp extends App:
  val app = Http.collectM {
    case Method.GET -> Root / "forecast" / "stock" / days =>
      ProductionSimulationService.simulateProductionTotals(days.toInt).map(prod =>
          Response.text(s"result = $prod"))
    case Method.GET -> Root / "forecast" / "suppliers" / days =>
      ProductionSimulationService.simulateSupplierSummaries(days.toInt).map(summaries =>
          Response.text(s"result = $summaries"))
    case r @ Method.POST -> Root / "suppliers" / "load" =>
      (for
        body <- ZIO.fromOption(r.getBodyAsString)
        _ <- SupplierState.resetState(SupplierPayload.fromString(body))
      yield Response.http(status = Status.RESET_CONTENT))
        .orElse(ZIO.succeed(Response.http(status = Status.INTERNAL_SERVER_ERROR)))
  }


  val layers = SupplierState.live >+> ProductionSimulationService.live

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    Server.start(8080, app)
      .provideCustomLayer(layers)
      .exitCode
  
