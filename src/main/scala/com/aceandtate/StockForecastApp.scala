package com.aceandtate

import zhttp.http._
import zhttp.service.Server
import zio._

object StockForecastApp extends App:
  val app: HttpApp[Any, Nothing] = Http.collect {
    case Method.GET -> Root / "forecast" / "stock" / days => Response.text(s"days = $days")
    case Method.GET -> Root / "forecast" / "suppliers" / days => ???
    case Method.POST -> Root / "suppliers" / "load" => ???
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    Server.start(8080, app.silent).exitCode
  
