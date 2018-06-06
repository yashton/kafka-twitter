package science.snelgrove.twitter.streams

import akka.http.scaladsl.model.headers.{ Authorization, GenericHttpCredentials, RawHeader }
import akka.http.scaladsl.model._
import akka.stream.Materializer
import scala.collection.JavaConverters._
import com.twitter.joauth.Normalizer;
import com.twitter.joauth.OAuthParams;
import com.twitter.joauth.Request.Pair;
import com.twitter.joauth.Signer;
import com.twitter.joauth.UrlCodec;
import java.net.URLEncoder
import java.security.SecureRandom;
import org.slf4j.LoggerFactory
import scala.concurrent.Future
import scala.util.Random

object OAuth {
  case class Consumer(key: String, secret: String)
  case class Token(key: String, secret: String)
}

class OAuth(consumer: OAuth.Consumer, token: OAuth.Token) {
  lazy val log = LoggerFactory.getLogger("oauth")

  val normalizer: Normalizer = Normalizer.getStandardNormalizer()
  val signer: Signer = Signer.getStandardSigner()
  val secureRandom: SecureRandom = new SecureRandom()

  def oauthHeader(uri: Uri, method: HttpMethod, form: FormData): HttpHeader = {
    val timestampSecs = generateTimestamp()
    val nonce = generateNonce()
    oauthCreateHeader(oauthRawHeader(uri, method, form, nonce, timestampSecs))
  }

  def oauthCreateHeader(params: Map[String, String]): HttpHeader = {
    val combined = params.map { case (k, v) => s"""$k=\"$v\"""" }.mkString(", ")
    RawHeader("Authorization", "OAuth " + combined)
  }

  def oauthRawHeader(uri: Uri, method: HttpMethod, form: FormData, nonce: String, timestampSecs: Long): Map[String, String] = {
    val javaParams = for ((k, v) <- form.fields) yield new Pair(UrlCodec.encode(k), UrlCodec.encode(v))

    val oAuth1Params: OAuthParams.OAuth1Params = new OAuthParams.OAuth1Params(
      token.key, consumer.key, nonce, timestampSecs, timestampSecs.toString, "",
      OAuthParams.HMAC_SHA1, OAuthParams.ONE_DOT_OH);

    val normalized: String = normalizer.normalize(
      uri.scheme,
      uri.authority.host.toString,
      uri.effectivePort,
      method.name,
      uri.path.toString,
      javaParams.asJava,
      oAuth1Params)

    val signature = signer.getString(normalized, token.secret, consumer.secret)

    Map(
      OAuthParams.OAUTH_CONSUMER_KEY -> consumer.key,
      OAuthParams.OAUTH_TOKEN -> token.key,
      OAuthParams.OAUTH_SIGNATURE -> signature,
      OAuthParams.OAUTH_SIGNATURE_METHOD -> OAuthParams.HMAC_SHA1,
      OAuthParams.OAUTH_TIMESTAMP -> timestampSecs.toString,
      OAuthParams.OAUTH_NONCE -> nonce,
      OAuthParams.OAUTH_VERSION -> OAuthParams.ONE_DOT_OH)
  }

  private def quoted(str: String): String = "\"" + str + "\""

  private def generateTimestamp(): Long = System.currentTimeMillis() / 1000

  private def generateNonce(): String =
    Math.abs(secureRandom.nextLong()).toString + System.currentTimeMillis()
}
