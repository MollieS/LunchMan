name := "play-java"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  evolutions,
  "com.opencsv" % "opencsv" % "3.8",
  "org.postgresql" % "postgresql" % "9.4.1208.jre7",
  "org.scoverage" % "sbt-scoverage" % "1.3.5",
  "org.scoverage" % "sbt-coveralls" % "1.1.0"
)

javaOptions in Test += "-Dconfig.file=conf/application.test.conf"

