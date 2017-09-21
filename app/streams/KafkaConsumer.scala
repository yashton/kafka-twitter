package science.snelgrove

import java.nio.charset.StandardCharsets
import org.apache.kafka.common.serialization._
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream._
import scala.collection.JavaConverters._
import play.api.libs.json._
import org.slf4j._

private class JsonSerde[T >: Null](implicit format: Format[T])  extends BaseSerde[T] {
  private val log = LoggerFactory.getLogger("json")
  override def deserialize(topic: String, data: Array[Byte]): T = {
    if (data == null) return null
    log.info("Deserialize " + new String(data, StandardCharsets.UTF_8))
    Json.parse(new String(data, StandardCharsets.UTF_8)).as[T]
  }

  override def serialize(topic: String, obj: T): Array[Byte] = {
    log.info(obj.toString)
    Json.toJson(obj).toString().getBytes(StandardCharsets.UTF_8)
  }
}

private class Utf8Serde extends BaseSerde[String] {
  override def deserialize(topic: String, data: Array[Byte]): String = {
    new String(data, StandardCharsets.UTF_8)
  }

  override def serialize(topic: String, obj: String): Array[Byte] = {
    obj.getBytes(StandardCharsets.UTF_8)
  }
}

private class IntSerde extends BaseSerde[Int] {
  override def deserialize(topic: String, data: Array[Byte]): Int = {
    new String(data, StandardCharsets.UTF_8).toInt
  }

  override def serialize(topic: String, obj: Int): Array[Byte] = {
    obj.toString.getBytes(StandardCharsets.UTF_8)
  }

}

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
