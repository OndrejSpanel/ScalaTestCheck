ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "ScalaTestCheck",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.11" % Test,
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.16.0" % Test,
    libraryDependencies += "org.typelevel" %% "scalacheck-effect" % "1.0.4" % Test,
    libraryDependencies += "org.scalatestplus" %% "scalacheck-1-15" % "3.2.11.0" % Test,
  )
