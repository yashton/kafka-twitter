package science.snelgrove

import akka.http.scaladsl.model._
import com.typesafe.config._
import java.nio.charset.StandardCharsets
import org.scalatest._
import Matchers._

class OAuthTest extends FlatSpec {
  // todo test values here rather than my actual secret...
  val token = OAuth.Token("10138782-lM0d0VFUFRHRUZQSEJvZzpMOHFxOVBaeVJnNmllS0",
    "xvz1evFS4wEEPTGEFPHBog:L8qq9PZyRg6ieKGEKhZolGC0vJWLw8iEJ88DRdyOg")
  val consumer = OAuth.Consumer("xvz1evFS4wEEPTGEFPHBog",
    "L8qq9PZyRg6ieKGEKhZolGC0vJWLw8iEJ88DRdyOg")
  val uri = Uri("https://stream.twitter.com/1.1/statuses/filter.json")
  val oauth = new OAuth(consumer, token)
  val form = FormData("track" -> "pokemon")

  val nonce = "87340131596360793811505760726369"
  val timestamp = 1505760726L

  it should "generate a signature" in {
    val params = oauth.oauthRawHeader(uri, HttpMethods.POST, form, nonce, timestamp)
    params should contain ("oauth_nonce" -> nonce)
    params should contain ("oauth_token" -> token.key)
    params should contain ("oauth_consumer_key" -> consumer.key)
    params should contain ("oauth_signature_method" -> "HMAC-SHA1")
    params should contain ("oauth_timestamp" -> timestamp.toString)
    params should contain ("oauth_version" -> "1.0")
    params should contain ("oauth_signature" -> "DUJbH3xCGxazv1%2B2hjaRPXV%2FBqs%3D")
  }

  it should "generate a header with formatted parameters" in {
    val header = oauth.oauthCreateHeader(Map(
      "squirtle" -> "wartortle",
      "bulbasaur" -> "ivysaur"))
    header.value shouldEqual """OAuth squirtle="wartortle", bulbasaur="ivysaur""""
  }
}
