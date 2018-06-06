package science.snelgrove.twitter.streams

import java.nio.charset.StandardCharsets
import org.apache.kafka.common.serialization._
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream._
import scala.collection.JavaConverters._
import play.api.libs.json._
import org.slf4j._

class KafkaConsumer(topic: String, topicOut: String, topicOutAvg: String, avgStoreName: String) {
  import Serialization._
  implicit private val tweetSerde = new JsonSerde[Tweet]()
  implicit private val intSerde = new IntSerde()
  implicit private val doubleSerde = new Serdes.DoubleSerde()
  implicit private val keySerde = new Utf8Serde()
  implicit private val tupleSerde = new JsonSerde[(Int, Double)]
  import KafkaStreamImplicits._

  def setup: KStreamBuilder = {
    val builder = new KStreamBuilder()

    val tweets = builder.streamS[String, Tweet](topic)

    tweets
      .mapValues[String](t => t.text)
      .sendTo(topicOut)

    val init: Initializer[(Int, Double)] = () => (0, 0)
    val avg: Aggregator[String, Int, (Int, Double)] =
      { case (k, v, (c, a)) => (c + 1, (a * c + v) / (c + 1)) }

    tweets
      .mapValues[Int](t => t.text split "\\W+" size)
      .groupByKey(implicitly[Serde[String]], implicitly[Serde[Int]])
      .agg[(Int, Double)](init, avg, avgStoreName)
      .sendTo(topicOutAvg)

    builder
  }
}
