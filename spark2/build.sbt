name := "spark2-http-rdd"

val sparkVersion = "2.4.6"

libraryDependencies ++= Seq(
  "org.apache.spark"          %% "spark-core" % sparkVersion % Provided,
  "org.apache.httpcomponents" % "httpclient"  % "4.5.13"
)

// test deps
libraryDependencies ++= Seq(
  "org.scalatest"   %% "scalatest"       % "3.2.3"  % "it",
  "org.mock-server" % "mockserver-netty" % "5.11.1" % "it"
)

dependencyOverrides ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind",
  "com.fasterxml.jackson.core" % "jackson-core",
  "com.fasterxml.jackson.core" % "jackson-annotations"
).map(_ % "2.6.7").map(_ % "it")
