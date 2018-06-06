package science.snelgrove.twitter.api

import akka.actor.{ ActorRef, ActorSystem }
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.delete
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.MethodDirectives.post
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import akka.util.Timeout
import de.heikoseeberger.akkahttpplayjson._
import play.api.libs.json._
import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.concurrent.duration._
import science.snelgrove.twitter.streams._

trait TweetRoutes extends PlayJsonSupport {
  lazy val log = Logging(system, classOf[TweetRoutes])

  implicit def system: ActorSystem
  def streams: KafkaStreamManager
  implicit lazy val blockingDispatcher = system.dispatchers.lookup("ktable-blocking-dispatcher")

  lazy val tweetRoutes: Route =
    pathPrefix("tweets") {
      concat(
        path("avg") {
          get {
            parameters("threshold".as[Int].?) { threshold =>
              complete {
                Future {
                  (for {
                    kv <- asScalaIterator(streams.avgStore.all())
                    if kv.value._1 >= threshold.getOrElse(0)
                  } yield kv.key -> kv.value._2).toMap
                }
              }
            }
          }
        },
        path("users" / ".*".r / "avg") { user =>
          get {
            complete {
              Future {
                streams.avgStore.get(user)._2
              }
            }
          }
        })
    }
}
