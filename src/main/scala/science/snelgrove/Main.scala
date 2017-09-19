package science.snelgrove

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

object Main extends App {
  val config = ConfigFactory.load()
  val token = OAuth.Token(config.getString("twitter.access.token"),
    config.getString("twitter.access.secret"))
  val consumer = OAuth.Consumer(config.getString("twitter.consumer.key"),
    config.getString("twitter.consumer.secret"))
  val twitter = new TwitterStream("twitter", consumer, token)
  twitter.start("pokemon")
  val kafka = new KafkaConsumer("twitter", "tweetsByUser", "userAverageWords")
  kafka.start()
}
