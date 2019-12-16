import sbt._
import Dependencies._

lazy val root = project("social", file("."))
  .settings(description := "Social Networking API by Backwards")

def project(id: String, base: File): Project =
  Project(id, base)
    .enablePlugins(JavaAppPackaging, DockerPlugin)
    .settings(
      scalaVersion := BuildProperties("scala.version"),
      sbtVersion := BuildProperties("sbt.version"),
      organization := "com.backwards",
      maintainer := "Backwards",
      name := id,
      autoStartServer := false,
      addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
      libraryDependencies ++= dependencies,
      scalacOptions += "-Ymacro-annotations",
      fork := true,
      run / connectInput := true,
      publishArtifact in Test := true,
      dockerRepository := Some("davidainslie"),
      dockerBaseImage := "openjdk:14-jdk",
      dockerLabels := Map("MAINTAINER" -> maintainer.value)
    )