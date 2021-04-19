ThisBuild / scalaVersion := "2.12.12"
ThisBuild / organization := "com.github.fsanaulla"
ThisBuild / parallelExecution := false

lazy val root = project
  .in(file("."))
  .aggregate(Seq(core, spark2, spark3, testing).flatMap(_.projectRefs): _*)

lazy val core = (projectMatrix in file("core"))
  .settings(name := "spark-http-rdd-core")
  .jvmPlatform(scalaVersions = Seq("2.11.8", "2.12.12"))

lazy val testing = (projectMatrix in file("testing"))
  .settings(
    name := "spark-http-rdd-testing",
    libraryDependencies ++= Seq(
      Dependencies.scalaTest,
      Dependencies.mockServerNetty,
      Dependencies.testContainersScala
    )
  )
  .jvmPlatform(scalaVersions = Seq("2.11.8", "2.12.12"))

lazy val spark2 = (projectMatrix in file("spark2"))
  .settings(
    name := "spark2-http-rdd",
    libraryDependencies ++= Seq(
      "org.apache.spark"          %% "spark-core" % "2.4.7" % Provided,
      "org.apache.httpcomponents" % "httpclient"  % "4.5.13"
    ),
    dependencyOverrides ++= Dependencies.jacksonOverrides
      .map(_ % Dependencies.Versions.jacksonForSpark2)
      .map(_ % "it")
  )
  .dependsOn(core)
  .dependsOn(testing % "it")
  .configure(itTestConfiguration)
  .jvmPlatform(scalaVersions = Seq("2.11.8", "2.12.12"))

lazy val spark3 = (projectMatrix in file("spark3"))
  .dependsOn(core)
  .settings(
    crossPaths := false,
    name := "spark3-http-rdd",
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-core" % "3.1.1" % Provided
    ),
    dependencyOverrides ++= Dependencies.jacksonOverrides
      .map(_ % Dependencies.Versions.jacksonForSpark3)
      .map(_ % "it")
  )
  .dependsOn(testing % "it")
  .configure(itTestConfiguration)
  .jvmPlatform(scalaVersions = Seq("2.12.12"))

def itTestConfiguration: Project => Project =
  _.settings(Defaults.itSettings).configs(IntegrationTest)
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
