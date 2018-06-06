lazy val akkaHttpVersion = "10.1.1"
lazy val akkaVersion = "2.5.12"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "science.snelgrove",
      scalaVersion := "2.12.6"
    )),
    mainClass := Some("science.snelgrove.twitter"),
    name := "kafka-twitter",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream-kafka" % "0.17",
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.chuusai" %% "shapeless" % "2.3.2",
      "com.twitter" % "hbc-core" % "2.2.0",
      "com.twitter" % "joauth" % "6.0.2",
      "com.typesafe.play" %% "play-json" % "2.6.7",
      "de.heikoseeberger" %% "akka-http-play-json" % "1.21.0",
      "org.apache.kafka" % "kafka-streams" % "0.11.0.0",
      "org.scalactic" %% "scalactic" % "3.0.1",
      "org.scalaz" %% "scalaz-core" % "7.2.15",

      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.1" % Test
    )
  )
