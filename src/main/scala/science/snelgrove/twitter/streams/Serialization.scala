package science.snelgrove.twitter.streams

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.JsonNaming.SnakeCase

object Serialization {
  implicit val config = JsonConfiguration(SnakeCase)

  val coordReads: Reads[Coordinates] = (
    (JsPath \ "coordinates" \ 0).read[Double] and
    (JsPath \ "coordinates" \ 1).read[Double])(Coordinates.apply _)

  val coordWrites: Writes[Coordinates] = new Writes[Coordinates] {
    def writes(c: Coordinates): JsValue = {
      Json.obj(
        "type" -> JsString("Point"),
        "coordinates" -> Json.arr(c.longitude, c.latitude))
    }
  }

  implicit val coordFormat = Format(coordReads, coordWrites)
  implicit val userFormat = Json.format[User]
  implicit val tweetFormat = Json.format[Tweet]

  implicit def tupleReads[A, B](implicit a: Reads[A], b: Reads[B]): Reads[(A, B)] =
    ((JsPath \ "a").read[A] and (JsPath \ "b").read[B])(Tuple2.apply[A, B] _)

  implicit def tupleWrites[A, B](implicit a: Writes[A], b: Writes[B]): Writes[(A, B)] =
    ((JsPath \ "a").write[A] and (JsPath \ "b").write[B])(unlift(Tuple2.unapply[A, B]))
}
