name := "kafka-play"
organization := "science.snelgrove"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

resolvers ++= Seq (
  Resolver.bintrayRepo("cakesolutions", "maven"),
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)

libraryDependencies ++= Seq (
  guice,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.chuusai" %% "shapeless" % "2.3.2",
  "com.twitter" % "hbc-core" % "2.2.0",
  "com.twitter" % "joauth" % "6.0.2",

  "com.typesafe.akka" %% "akka-actor" % "2.5.4",
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "com.typesafe.akka" %% "akka-stream" % "2.5.4",
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.17",
  "org.apache.kafka" % "kafka-streams" % "0.11.0.0",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalaz" %% "scalaz-core" % "7.2.15",

  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.5.4" % Test,
  "org.scalatest" %% "scalatest" % "3.0.1" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
)


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "science.snelgrove.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "science.snelgrove.binders._"
