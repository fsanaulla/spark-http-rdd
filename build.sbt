ThisBuild / scalaVersion := "2.12.4"
ThisBuild / organization := "com.github.fsanaulla"
ThisBuild / parallelExecution := false

lazy val root = project
  .in(file("."))
  .aggregate(core, spark2, spark3, testing)

lazy val core = project

lazy val testing = project

lazy val spark2 = project
  .dependsOn(core)
  .configure(itTestConfiguration)

lazy val spark3 = project
  .dependsOn(core)
  .configure(itTestConfiguration)

def itTestConfiguration: Project => Project =
  _.settings(Defaults.itSettings)
    .configs(IntegrationTest)
    .dependsOn(testing % "it")
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
