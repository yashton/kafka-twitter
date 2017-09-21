package controllers

import javax.inject._
import org.apache.kafka.streams._
import org.apache.kafka.streams.state._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.collection.JavaConverters._
import science.snelgrove._

@Singleton
class HomeController @Inject()(cc: ControllerComponents, streams: KafkaStreamManager) extends AbstractController(cc) {
  import Serialization.tupleWrites
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  def avgWords() = avgWordsThreshold(0)

  def avgWordsThreshold(count: Int) = Action { request: Request[AnyContent] =>
    val store = streams.avgStore
    val data = for(kv <- asScalaIterator(store.all()) if kv.value._1 >= count) yield kv.key -> kv.value
    Ok(Json.toJson(data.toList))
  }

  def avgWordsForUser(user: String) = Action { request: Request[AnyContent] =>
    val store = streams.avgStore
    Ok(Json.toJson(store.get(user)))
  }
}
