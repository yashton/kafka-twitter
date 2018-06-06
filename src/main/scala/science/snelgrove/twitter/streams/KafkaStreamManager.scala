package science.snelgrove.twitter.streams

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import java.util.Properties
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream._
import org.apache.kafka.streams.state._

class KafkaStreamManager() {
  val config = ConfigFactory.load()

  val kafkaConsumer = new KafkaConsumer(
    config.getString("twitter.topics.tweets"),
    config.getString("twitter.topics.tweetsByUser"),
    config.getString("twitter.topics.userAverages"),
    config.getString("twitter.store.averages"))

  private val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-test")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, config.getString("kafka.server"))

  val kafka = new KafkaStreams(kafkaConsumer.setup, props)
  kafka.start()

  def avgStore(): ReadOnlyKeyValueStore[String, (Int, Double)] = {
    kafka.store(
      config.getString("twitter.store.averages"),
      QueryableStoreTypes.keyValueStore[String, (Int, Double)]());
  }
}
