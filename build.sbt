ThisBuild / scalaVersion := "2.12.4"
ThisBuild / organization := "com.github.fsanaulla"
ThisBuild / organizationName := "spark-http-rdd"

lazy val root = (project in file("."))
  .settings(
    name := "spark-http-rdd",
    libraryDependencies ++= Seq(
      "org.apache.spark"             %% "spark-core"           % "2.4.6" % Provided,
      "org.apache.httpcomponents"    % "httpclient"            % "4.5.13",
      "com.fasterxml.jackson.core"   % "jackson-databind"      % "2.10.5",
      "com.fasterxml.jackson.core"   % "jackson-core"          % "2.10.5",
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.10.5",
      "org.scalatest"                %% "scalatest"            % "3.2.3" % "test,it",
      "org.mock-server"              % "mockserver-netty"      % "5.11.1" % "it",
      "org.apache.spark"             %% "spark-sql"            % "2.4.6" % "it"
    )
  )
  .settings(Defaults.itSettings)
  .configs(IntegrationTest)

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
