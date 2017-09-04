name := "discala"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.1"

resolvers += Resolver.bintrayRepo("commercetools", "maven")

libraryDependencies ++= Seq(
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "com.neovisionaries" % "nv-websocket-client" % "2.3"
)

val circeVersion = "0.8.0"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-optics",
  "io.circe" %% "circe-literal"
).map(_ % circeVersion)

addCommandAlias("start", "reStart")
addCommandAlias("stop", "reStop")