import Dependencies._

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "science.snelgrove",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "kafka-demo",

    resolvers ++= Seq (
      Resolver.bintrayRepo("cakesolutions", "maven"),
      "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
    ),
    libraryDependencies ++= Seq (
      "net.cakesolutions" %% "scala-kafka-client-akka" % "0.11.0.0",
      "net.cakesolutions" %% "scala-kafka-client-testkit" % "0.11.0.0" % Test,
      "com.twitter" % "hbc-core" % "2.2.0",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.akka" %% "akka-stream-kafka" % "0.17",
      "com.typesafe.play" %% "play-json" % "2.6.3",
      "com.typesafe" % "config" % "1.3.1",
      "com.typesafe.akka" %% "akka-actor" % "2.5.4",
      "com.typesafe.akka" %% "akka-testkit" % "2.5.4" % Test,
      "com.typesafe.akka" %% "akka-stream" % "2.5.4",
      "com.typesafe.akka" %% "akka-http" % "10.0.10",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,
      "org.scalactic" %% "scalactic" % "3.0.1",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      "org.scalaz" %% "scalaz-core" % "7.2.15",
      "com.chuusai" %% "shapeless" % "2.3.2",
      "com.typesafe.akka" %% "akka-stream-kafka" % "0.17",
      "com.twitter" % "joauth" % "6.0.2",
      "org.apache.kafka" % "kafka-streams" % "0.11.0.0"
    )
  )
