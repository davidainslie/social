package com.backwards.social

import cats.effect._
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze._

trait RunnerApp extends IOApp {
  def routes: HttpRoutes[IO]

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(Router("/" -> routes).orNotFound)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}