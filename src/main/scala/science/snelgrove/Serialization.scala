package science.snelgrove

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.libs.json.JsonNaming.SnakeCase

object Serialization {
  implicit val config = JsonConfiguration(SnakeCase)

  val coordReads: Reads[Coordinates] = (
    (JsPath \ "coordinates" \ 0).read[Double] and
      (JsPath \ "coordinates" \ 1).read[Double]
  )(Coordinates.apply _)

  val coordWrites: Writes[Coordinates] = new Writes[Coordinates] {
    def writes(c: Coordinates): JsValue = {
      Json.obj(
        "type" -> JsString("Point"),
        "coordinates" -> Json.arr(c.longitude, c.latitude))
    }
  }

  implicit val coordFormat: Format[Coordinates] =
    Format(coordReads, coordWrites)
  implicit val userFormat = Json.format[User]
  implicit val tweetFormat = Json.format[Tweet]
}
