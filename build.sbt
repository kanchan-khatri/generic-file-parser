
name := "RecordsParser"

version := "0.1"

scalaVersion := "2.12.8"
resolvers ++= Seq(
  "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/",
  "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.apache.spark" %% "spark-core"  % "2.4.5",
  "org.apache.spark" %% "spark-sql"  % "2.4.5"
)
fork := true
run := Defaults.runTask(fullClasspath in Runtime, mainClass in run in Compile, runner in run).evaluated
