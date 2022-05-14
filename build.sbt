name := """stock-trade"""
organization := "hcl"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  "mysql"                   %  "mysql-connector-java"      % "8.0.18",
  "com.typesafe.play"       %% "play-slick"                % "5.0.0",
  "com.typesafe.play"       %% "play-slick-evolutions"     % "5.0.0"
)

libraryDependencies ++= Seq(
  guice, ws
)
