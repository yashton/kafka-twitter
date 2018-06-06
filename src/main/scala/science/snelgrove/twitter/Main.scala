package science.snelgrove.twitter

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
import akka.http.scaladsl.server.Route
import com.typesafe.config.ConfigFactory
import java.util.Properties
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream._
import science.snelgrove.twitter.api._
import science.snelgrove.twitter.streams._
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import akka.http.scaladsl.Http

object Main extends App with TweetRoutes {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val config = ConfigFactory.load()

  val token = OAuth.Token(
    config.getString("twitter.access.token"),
    config.getString("twitter.access.secret"))
  val consumer = OAuth.Consumer(
    config.getString("twitter.consumer.key"),
    config.getString("twitter.consumer.secret"))
  val twitter = new TwitterStream(config.getString("twitter.topics.tweets"), consumer, token)

  twitter.start("pokemon")

  val streams = new KafkaStreamManager()

  lazy val routes: Route = tweetRoutes
  Http().bindAndHandle(routes, "localhost", 8080)
  Await.result(system.whenTerminated, Duration.Inf)
}
