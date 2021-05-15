ThisBuild / scalaVersion := "2.12.13"
ThisBuild / organization := "com.github.fsanaulla"

lazy val `spark-http-rdd` = project
  .in(file("."))
  .aggregate(Seq(core, spark2, spark3, testing).flatMap(_.projectRefs): _*)

lazy val core = (projectMatrix in file("core"))
  .settings(name := "spark-http-rdd-core")
  .configure(defaultConfiguration)
  .jvmPlatform(scalaVersions = Seq("2.11.8", "2.12.13"))

lazy val testing = (projectMatrix in file("testing"))
  .settings(
    name := "spark-http-rdd-testing",
    libraryDependencies ++= Seq(
      Dependencies.scalaTest,
      Dependencies.testContainersScala
    )
  )
  .configure(defaultConfiguration)
  .jvmPlatform(scalaVersions = Seq("2.11.8", "2.12.13"))

lazy val spark2 = (projectMatrix in file("spark2"))
  .settings(
    name := "spark2-http-rdd",
    libraryDependencies ++= Seq(
      "org.apache.spark"         %% "spark-core" % "2.4.8" % Provided,
      "org.apache.httpcomponents" % "httpclient" % "4.5.13"
    )
  )
  .dependsOn(core)
  .dependsOn(testing % "it")
  .configure(defaultConfiguration)
  .configure(itTestConfiguration)
  .jvmPlatform(scalaVersions = Seq("2.11.8", "2.12.13"))

lazy val spark3 = (projectMatrix in file("spark3"))
  .settings(
    crossPaths := false,
    name := "spark3-http-rdd",
    libraryDependencies += "org.apache.spark" %% "spark-core" % "3.1.1" % Provided
  )
  .dependsOn(core)
  .dependsOn(testing % "it")
  .configure(defaultConfiguration)
  .configure(itTestConfiguration)
  .jvmPlatform(scalaVersions = Seq("2.12.13"))

def defaultConfiguration: Project => Project =
  _.settings(Settings.base).enablePlugins(AutomateHeaderPlugin)

def itTestConfiguration: Project => Project =
  _.settings(Defaults.itSettings).configs(IntegrationTest)
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
