import sbt._

object Dependencies {
  lazy val dependencies: Seq[ModuleID] =
    Seq(
      scalatest, mockitoScalatest, scalacheck, cats, simulacrum, refined, monocle, shapeless, http4s, sttp, circe
    ).flatten
  
  lazy val scalatest: Seq[ModuleID] =
    Seq("org.scalatest" %% "scalatest" % "3.1.0" % "test")

  lazy val mockitoScalatest: Seq[ModuleID] =
    Seq("org.mockito" %% "mockito-scala-scalatest" % "1.10.0" % "test")

  lazy val scalacheck: Seq[ModuleID] =
    Seq("org.scalacheck" %% "scalacheck" % "1.14.3" % "test")

  lazy val cats: Seq[ModuleID] = {
    val group = "org.typelevel"
    val version = "2.0.0"

    Seq(
      "cats-core", "cats-effect"
    ).map(group %%  _ % version) ++ Seq(
      "cats-laws", "cats-testkit"
    ).map(group %%  _ % version % "test") ++ Seq(
      "dev.profunktor" %% "console4cats" % "0.8.0"
    ) ++ Seq(
      "io.chrisdavenport" %% "cats-scalacheck" % "0.2.0" % "test"
    )
  }
  
  lazy val simulacrum: Seq[ModuleID] =
    Seq("com.github.mpilquist" %% "simulacrum" % "0.19.0")
  
  lazy val refined: Seq[ModuleID] =
    Seq(
      "refined", "refined-pureconfig", "refined-cats"
    ).map("eu.timepit" %%  _ % "0.9.10")

  lazy val monocle: Seq[ModuleID] = {
    val group = "com.github.julien-truffaut"
    val version = "2.0.0"

    Seq(
      "monocle-core", "monocle-macro", "monocle-generic"
    ).map(group %% _ % version) ++ Seq(
      "monocle-law"
    ).map(group %% _ % version % "test")
  }

  lazy val shapeless: Seq[ModuleID] =
    Seq("com.chuusai" %% "shapeless" % "2.3.3")

  lazy val http4s: Seq[ModuleID] =
    Seq(
      "http4s-core", "http4s-server", "http4s-blaze-server", "http4s-dsl", "http4s-circe"
    ).map("org.http4s" %% _ % "0.21.0-M6")

  lazy val sttp: Seq[ModuleID] =
    Seq(
      "core", "httpclient-backend", "http4s-backend", "async-http-client-backend-cats", "circe"
    ).map("com.softwaremill.sttp.client" %% _ % "2.0.0-RC5")

  lazy val circe: Seq[ModuleID] = {
    val group = "io.circe"
    val version = "0.12.3"

    Seq(
      "circe-core", "circe-generic", "circe-parser", "circe-literal", "circe-shapes"
    ).map(group %% _ % version) ++ Seq(
      "circe-generic-extras"
    ).map(group %% _ % "0.12.2") ++ Seq(
      "circe-optics"
    ).map(group %% _ % "0.12.0") ++ Seq(
      "circe-testing"
    ).map(group %% _ % version % "test")
  }
}