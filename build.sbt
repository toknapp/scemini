name := """scemini"""

version := "1.0-SNAPSHOT"

scalaVersion in Scope.GlobalScope := "2.12.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % V.akkaHttpV,
  "com.typesafe.akka" %% "akka-stream" % V.akkaV,
  "io.circe" %% "circe-core" % V.circleV,
  "io.circe" %% "circe-parser" % V.circleV,
  "org.scalatest" %% "scalatest" % V.scalaTestV % "test",
  "com.typesafe.akka" %% "akka-stream-testkit" % V.akkaV % "test",
  "com.typesafe.akka" %% "akka-testkit" % V.akkaV % "test"
)



