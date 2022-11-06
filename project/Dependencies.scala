import sbt._

object Dependencies {

  object Versions {
    val scalaTest           = "3.2.12"
    val mockServerNetty     = "5.11.1"
    val testContainersScala = "0.40.11"
  }

  val scalaTest = "org.scalatest" %% "scalatest" % Versions.scalaTest
  val testContainersScala =
    "com.dimafeng" %% "testcontainers-scala-scalatest" % Versions.testContainersScala
}
