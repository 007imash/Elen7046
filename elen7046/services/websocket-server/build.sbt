name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies += jdbc
libraryDependencies += cache
libraryDependencies += ws
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test

libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-stream-kafka" % "0.13",
    "com.github.jkutner" % "env-keystore" % "0.1.2"
)

libraryDependencies += "org.apache.kafka" % "kafka_2.11" % "0.10.2.1"

cancelable in Global := true


