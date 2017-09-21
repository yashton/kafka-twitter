package science.snelgrove

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import java.util.Properties
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream._

object Main extends App {
  val config = ConfigFactory.load()

  val token = OAuth.Token(config.getString("twitter.access.token"),
    config.getString("twitter.access.secret"))
  val consumer = OAuth.Consumer(config.getString("twitter.consumer.key"),
    config.getString("twitter.consumer.secret"))
  val twitter = new TwitterStream(config.getString("twitter.topics.tweets"), consumer, token)
  twitter.start("pokemon")
/*
  val kafkaConsumer = new KafkaConsumer(
    config.getString("twitter.topics.tweets"),
    config.getString("twitter.topics.tweetsByUser"),
    config.getString("twitter.topics.userAverages"),
    "tweet-averages")

  val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-test")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")

  val kafka = new KafkaStreams(kafkaConsumer.setup, props)
  kafka.start()
 */
}
