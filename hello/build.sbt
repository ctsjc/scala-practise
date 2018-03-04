import Dependencies._
import sbt.Keys.libraryDependencies

scalaVersion := "2.12.2"
lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
    libraryDependencies += scalaTest % Test
  )
// https://mvnrepository.com/artifact/org.json4s/json4s-jackson
libraryDependencies += "org.json4s" % "json4s-jackson_2.12" % "3.6.0-M1"

// https://mvnrepository.com/artifact/org.json4s/json4s-native
libraryDependencies += "org.json4s" % "json4s-native_2.12" % "3.6.0-M1"

// https://mvnrepository.com/artifact/org.specs2/specs2
libraryDependencies += "org.specs2" % "specs2_2.10" % "1.12.3" % "test"
libraryDependencies += "org.mongodb" %% "casbah" % "3.1.1"

// https://mvnrepository.com/artifact/edu.stanford.nlp/stanford-corenlp
libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.8.0"
libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.8.0" classifier "models"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0"
