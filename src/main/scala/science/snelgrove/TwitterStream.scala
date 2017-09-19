package science.snelgrove

import akka.actor.ActorSystem
import akka.Done
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, Uri, FormData, HttpMethods}
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Source, Sink, Framing}
import akka.util.ByteString
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import play.api.libs.json.{Json, JsResultException}
import org.slf4j.LoggerFactory
import scala.collection.immutable._
import scala.concurrent.Future

class TwitterStream (topic: String, consumer: OAuth.Consumer, token: OAuth.Token) {
  implicit val system = ActorSystem()
  implicit val dispatcher = system.dispatcher
  implicit val materializer = ActorMaterializer()
  lazy val log = LoggerFactory.getLogger("tweets")
  val uri = Uri("https://stream.twitter.com/1.1/statuses/filter.json")
  val http = Http(system)

  val producerSettings = ProducerSettings(system, new StringSerializer, new StringSerializer)
    .withBootstrapServers("127.0.0.1:9092")

  def authenticate = new OAuth(consumer, token)
  import Serialization._

  def createRequest(query: String): HttpRequest = {
    val body = FormData("track" -> query)
    val authHeader = authenticate.oauthHeader(uri, HttpMethods.POST, body)
    log.info(authHeader.toString)
    HttpRequest(HttpMethods.POST, uri = uri,
      headers = Seq(authHeader), entity = body.toEntity)
  }

  def start(query: String): Future[Done] = {
    log.info("Starting up twitter")
    val request = createRequest(query)
    val response = http.singleRequest(request)
    log.info(request.toString)
    val source = response.map { r => log.info(r.toString); r }
      .map(_.entity.withoutSizeLimit.dataBytes)

    Source.fromFutureSource(source)
      .via(Framing.delimiter(ByteString("\r\n"), Int.MaxValue))
      .filter(_.nonEmpty)
      .map { _.utf8String }
      .take(10)
      .map { line =>
        try {
          val tweet = Json.parse(line).as[Tweet]
          log.debug("Got tweet " + tweet.id)
          new ProducerRecord[String, String](topic, tweet.user.id.toString, line)
        } catch {
          case e: JsResultException => {
            log.error("Failed to parse", e)
            log.error(line)
            throw e
          }
        }
      }
      .runWith(Producer.plainSink(producerSettings))
  }
}
