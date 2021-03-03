name := "spark2-http-rdd"

val sparkVersion = "2.4.6"

libraryDependencies ++= Seq(
  "org.apache.spark"          %% "spark-core" % sparkVersion % Provided,
  "org.apache.httpcomponents" % "httpclient"  % "4.5.13"
)

// test deps
libraryDependencies ++= Seq(
  "org.scalatest"   %% "scalatest"       % "3.2.3"  % "it",
  "org.mock-server" % "mockserver-netty" % "5.11.1" % "it" excludeAll (
    ExclusionRule("com.fasterxml.jackson.core", "jackson-annotations"),
    ExclusionRule("com.fasterxml.jackson.core", "jackson-core"),
    ExclusionRule("com.fasterxml.jackson.core", "jackson-databind")
  )
)
