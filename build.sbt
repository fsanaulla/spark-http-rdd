ThisBuild / scalaVersion := "2.12.4"
ThisBuild / organization := "com.github.fsanaulla"

lazy val root = project
  .in(file("."))
  .aggregate(core, spark2, spark3, testing)
  .settings(parallelExecution in IntegrationTest := false)

lazy val core = project

lazy val testing = project

lazy val spark2 = project
  .dependsOn(core)
  .settings(Defaults.itSettings)
  .configs(IntegrationTest)
  .dependsOn(testing % "it")

lazy val spark3 = project
  .dependsOn(core)
  .settings(Defaults.itSettings)
  .configs(IntegrationTest)
  .dependsOn(testing % "it")

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
