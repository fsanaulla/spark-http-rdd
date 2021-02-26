name := "spark3-http-rdd"

val sparkVersion = "3.1.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % Provided
)

// test deps
libraryDependencies ++= Seq(
  "org.scalatest"    %% "scalatest"       % "3.2.3"      % "it",
  "org.mock-server"  % "mockserver-netty" % "5.11.1"     % "it",
  "org.apache.spark" %% "spark-sql"       % sparkVersion % "it"
)
