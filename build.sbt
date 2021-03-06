import com.typesafe.sbt.rjs.Import.RjsKeys._
import com.typesafe.sbt.web.Import.WebJs._
import play.PlayImport.PlayKeys._

name := """commith4ck"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala).enablePlugins(SbtWeb)

scalaVersion := "2.11.2"

herokuAppName in Compile := "commith4ck"

herokuJdkVersion in Compile := "1.8"

pipelineStages := Seq(rjs)

requireNativePath := Some("node_modules/bin/r.js")

buildProfile := JS.Object("skipDirOptimize" -> true, "optimizeAllPluginResources" -> true)

libraryDependencies ++= Seq(
  filters,
  "org.eclipse.jgit" % "org.eclipse.jgit" % "3.5.0.201409071800-rc1",
  "commons-io" % "commons-io" % "2.4",
  "com.jsuereth" %% "scala-arm" % "1.4",
  "io.searchbox" % "jest" % "0.1.2",
  "org.elasticsearch" % "elasticsearch" % "1.3.2" % "test",
  "org.elasticsearch" % "elasticsearch" % "1.3.2" % "test" classifier "tests",
  "org.apache.lucene" % "lucene-test-framework" % "4.9.0" % "test"
)