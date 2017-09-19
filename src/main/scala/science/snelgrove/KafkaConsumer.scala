package science.snelgrove

import java.nio.charset.StandardCharsets
import java.util.Properties
import org.apache.kafka.common.serialization._
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream._
import play.api.libs.json._

private class JsonSerde[T](implicit format: Format[T])  extends BaseSerde[T] {
  override def deserialize(topic: String, data: Array[Byte]): T = {
    Json.parse(new String(data, StandardCharsets.UTF_8)).as[T]
  }

  override def serialize(topic: String, obj: T): Array[Byte] = {
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

private class LongSerde extends BaseSerde[Long] {
  override def serialize(topic: String, data: Long): Array[Byte] = {
    data.toString.getBytes("UTF-8")
  }

  override def deserialize(topic: String, data: Array[Byte]): Long = {
      new String(data, "UTF-8").toLong
  }
}

class KafkaConsumer(topic: String, topicOut: String) {

  def start(): Unit = {
    import Serialization._
    implicit val tweetSerde = new JsonSerde[Tweet]()
    implicit val keySerde = new Utf8Serde()
    implicit val longSerde = new LongSerde()

    val props = new Properties()
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-test")
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    val builder = new KStreamBuilder()
    val stream = builder.stream(implicitly[Serde[String]], implicitly[Serde[Tweet]], topic)
    stream.to(implicitly[Serde[String]], implicitly[Serde[Tweet]], topicOut)

    val kafka = new KafkaStreams(builder, props)
    kafka.start()
  }
}
