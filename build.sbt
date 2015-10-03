name := """wifi-tracker-web"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.google.code.gson" % "gson" % "2.3.1",
  "com.typesafe.play" %% "anorm" % "2.4.0",
  "com.sksamuel.scrimage" % "scrimage-core_2.11" % "2.1.0"
)

libraryDependencies ++= Seq(
  specs2 % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.3.13" % "test"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
