name := "glidertracker"

version := "1.0"

lazy val `glidertracker` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  jdbc,
  ehcache,
  ws,
  specs2 % Test,
  guice,
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.20"
)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

      