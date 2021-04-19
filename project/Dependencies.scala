import sbt._

object Dependencies {
  object Versions {
    val scalaTest           = "3.2.7"
    val mockServerNetty     = "5.11.1"
    val testContainersScala = "0.39.3"
    val jacksonForSpark2    = "2.7.0"
    val jacksonForSpark3    = "2.10.0"
  }

  val scalaTest           = "org.scalatest"   %% "scalatest"            % Versions.scalaTest
  val mockServerNetty     = "org.mock-server" % "mockserver-netty"      % Versions.mockServerNetty
  val testContainersScala = "com.dimafeng"    %% "testcontainers-scala" % Versions.testContainersScala

  val jacksonOverrides = Seq(
    "com.fasterxml.jackson.core" % "jackson-databind",
    "com.fasterxml.jackson.core" % "jackson-core",
    "com.fasterxml.jackson.core" % "jackson-annotations"
  )
}
