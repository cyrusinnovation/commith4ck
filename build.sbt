name := """commith4ck"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.eclipse.jgit" % "org.eclipse.jgit" % "3.5.0.201409071800-rc1",
  "commons-io" % "commons-io" % "2.4",
  "com.jsuereth" %% "scala-arm" % "1.4"
)
