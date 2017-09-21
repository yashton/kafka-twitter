package science.snelgrove

import javax.inject._
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import java.util.Properties
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream._
import org.apache.kafka.streams.state._

class KafkaStreamManager @Inject() {
  val config = ConfigFactory.load()

  val kafkaConsumer = new KafkaConsumer(
    config.getString("twitter.topics.tweets"),
    config.getString("twitter.topics.tweetsByUser"),
    config.getString("twitter.topics.userAverages"),
    "tweet-averages")

  private val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-test")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")

  val kafka = new KafkaStreams(kafkaConsumer.setup, props)
  kafka.start()

  def avgStore(): ReadOnlyKeyValueStore[String, (Int, Double)] = {
    kafka.store("tweet-averages", QueryableStoreTypes.keyValueStore[String, (Int, Double)]());
  }
}
