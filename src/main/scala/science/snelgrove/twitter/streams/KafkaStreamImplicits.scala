package science.snelgrove.twitter.streams

import org.apache.kafka.common.serialization._
import org.apache.kafka.streams._
import org.apache.kafka.streams.kstream._

object KafkaStreamImplicits {
  implicit class KStreamBuilderWrapper(builder: KStreamBuilder) {
    def streamS[K, V](topic: String)(implicit ks: Serde[K], vs: Serde[V]): KStream[K, V] =
      {
        builder.stream(ks, vs, topic)
      }
  }

  implicit class KStreamWrapper[K, V](stream: KStream[K, V]) {
    def sendTo(topic: String)(implicit ks: Serde[K], vs: Serde[V]): Unit =
      {
        stream.to(ks, vs, topic)
      }
  }

  implicit class KTableWrapper[K, V](table: KTable[K, V]) {
    def sendTo(topic: String)(implicit ks: Serde[K], vs: Serde[V]): Unit =
      {
        table.to(ks, vs, topic)
      }
  }

  implicit class KGroupedStreamWrapper[K, V](stream: KGroupedStream[K, V]) {
    def agg[U](init: Initializer[U], agg: Aggregator[K, V, U], store: String)(implicit s: Serde[U]): KTable[K, U] =
      {
        stream.aggregate(init, agg, s, store)
      }
  }
}
