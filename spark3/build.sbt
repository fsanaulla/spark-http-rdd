name := "spark3-http-rdd"

val sparkVersion = "3.1.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % Provided
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
