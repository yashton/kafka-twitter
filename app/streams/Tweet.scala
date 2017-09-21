package science.snelgrove
import java.time.Instant

case class Coordinates(longitude: Double, latitude: Double)
case class User(
  description: Option[String],
  followersCount: Int,
  friendsCount: Int,
  id: Long,
  location: Option[String],
  name: String,
  screenName: String,
  statusesCount: Int
)

sealed trait TwitterMsg

case class Tweet (
  id: Long,
  text: String,
  createdAt: String,
  coordinates: Option[Coordinates],
  favoriteCount: Int,
  retweetCount: Int,
  retweeted: Boolean,
  retweetedStatus: Option[Tweet],
  user: User
) extends TwitterMsg


case class Blank() extends TwitterMsg
case class Delete() extends TwitterMsg
case class ScrubGeo() extends TwitterMsg
case class Limit() extends TwitterMsg
case class StatusWithheld() extends TwitterMsg
case class UserWithheld() extends TwitterMsg
case class Disconnect() extends TwitterMsg
case class StallWarning() extends TwitterMsg
case class UserUpdate() extends TwitterMsg

sealed trait UserMsg
case class Friends() extends UserMsg
case class Direct() extends UserMsg
case class Event() extends UserMsg
case class FollowWarning() extends UserMsg
