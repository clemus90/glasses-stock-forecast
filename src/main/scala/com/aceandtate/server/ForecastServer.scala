package com.aceandtate.server

import cats._
import cats.effect._
import cats.implicits._
import org.http4s.circe._
import org.http4s._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.dsl._
import org.http4s.dsl.impl._
import org.http4s.headers._
import org.http4s.implicits._
import org.http4s.server._

object ForecastServer:
  def forecastRoutes[F[_]: Monad]: HttpRoutes[F] =
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case GET -> Root / "forecast" / "stock" / days => ???
      case GET -> Root / "forecast" / "suppliers" / days => ???
    }

  def supplierRoutes[F[_]: Monad]: HttpRoutes[F] =
    val dsl = Http4sDsl[F]
    import dsl._

    HttpRoutes.of[F] {
      case POST -> Root / "suppliers" / "load" => ???
    }

  def allRoutes[F[_]: Monad]: HttpApp[F] =
    (forecastRoutes <+> supplierRoutes).orNotFound
