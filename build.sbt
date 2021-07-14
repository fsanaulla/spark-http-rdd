ThisBuild / scalaVersion := "2.12.14"
ThisBuild / organization := "com.github.fsanaulla"
ThisBuild / homepage := Some(url(s"${Owner.github}/${Owner.projectName}"))
ThisBuild / developers += Developer(
  id = Owner.id,
  name = Owner.fullName,
  email = Owner.email,
  url = url(Owner.github)
)

// publish
ThisBuild / scmInfo := Some(
  ScmInfo(
    url(s"${Owner.github}/${Owner.projectName}"),
    s"scm:git@github.com:${Owner.id}/${Owner.projectName}.git"
  )
)
ThisBuild / publishTo := sonatypePublishToBundle.value
ThisBuild / sonatypeBundleDirectory := (ThisBuild / baseDirectory).value / "target" / "sonatype-staging" / s"${version.value}"
ThisBuild / sonatypeProjectHosting := Some(
  GitHubHosting(Owner.github, Owner.projectName, Owner.email)
)
ThisBuild / pgpPassphrase := sys.env.get("PGP_PASSPHRASE").map(_.toCharArray)

lazy val `spark-http-rdd` = project
  .in(file("."))
  .configure(license)
  .aggregate(Seq(core, spark2, spark3, testing).flatMap(_.projectRefs): _*)

lazy val core = (projectMatrix in file("core"))
  .settings(name := "spark-http-rdd-core")
  .jvmPlatform(scalaVersions = Seq("2.11.8", "2.12.14"))

lazy val testing = (projectMatrix in file("testing"))
  .settings(
    name := "spark-http-rdd-testing",
    publish / skip := true,
    libraryDependencies ++= Seq(
      Dependencies.scalaTest,
      Dependencies.testContainersScala
    )
  )
  .jvmPlatform(scalaVersions = Seq("2.11.8", "2.12.14"))

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
  .configure(itTestConfiguration)
  .jvmPlatform(scalaVersions = Seq("2.11.8", "2.12.14"))

lazy val spark3 = (projectMatrix in file("spark3"))
  .settings(
    crossPaths := false,
    name := "spark3-http-rdd",
    libraryDependencies += "org.apache.spark" %% "spark-core" % "3.1.1" % Provided
  )
  .dependsOn(core)
  .dependsOn(testing % "it")
  .configure(itTestConfiguration)
  .jvmPlatform(scalaVersions = Seq("2.12.14"))

def license: Project => Project =
  _.settings(
    startYear := Some(2021),
    headerLicense := Some(HeaderLicense.ALv2("2021", Owner.fullName))
  ).enablePlugins(AutomateHeaderPlugin)

def itTestConfiguration: Project => Project =
  _.settings(Defaults.itSettings).configs(IntegrationTest)
// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
