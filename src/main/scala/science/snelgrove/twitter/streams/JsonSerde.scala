package science.snelgrove.twitter.streams

import java.nio.charset.StandardCharsets
import org.apache.kafka.common.serialization._
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream._
import scala.collection.JavaConverters._
import play.api.libs.json._
import org.slf4j._

class JsonSerde[T >: Null](implicit format: Format[T]) extends BaseSerde[T] {
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

class Utf8Serde extends BaseSerde[String] {
  override def deserialize(topic: String, data: Array[Byte]): String = {
    new String(data, StandardCharsets.UTF_8)
  }

  override def serialize(topic: String, obj: String): Array[Byte] = {
    obj.getBytes(StandardCharsets.UTF_8)
  }
}

class IntSerde extends BaseSerde[Int] {
  override def deserialize(topic: String, data: Array[Byte]): Int = {
    new String(data, StandardCharsets.UTF_8).toInt
  }

  override def serialize(topic: String, obj: Int): Array[Byte] = {
    obj.toString.getBytes(StandardCharsets.UTF_8)
  }

}
