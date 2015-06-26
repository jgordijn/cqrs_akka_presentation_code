import sbt.Opts.resolver

name := "Example Project"

version := "1.0"

scalaVersion := "2.11.6"

resolvers += "processFramework at bintray" at "https://dl.bintray.com/jgordijn/maven/"


val akkaVersion = "2.3.11"
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-experimental" % akkaVersion,
  "processframework" %% "process" % "0.1.15",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)
