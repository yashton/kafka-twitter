package science.snelgrove.twitter.streams

import akka.http.scaladsl.model._
import com.typesafe.config._
import java.nio.charset.StandardCharsets
import org.scalatest._
import Matchers._
import play.api.libs.json._
import science.snelgrove.twitter.streams.Tweet
class SerializationTest extends FlatSpec {
  import Serialization._

  it should "deserialize a sample tweet" in {
    val json = Json.parse(tweet).as[Tweet]
    json.user.id shouldEqual 799087176218800100L
  }

  val tweet =
    """
{
  "created_at": "Mon Sep 18 19:10:42 +0000 2017",
  "id": 909857230668759000,
  "id_str": "909857230668759045",
  "text": "Trump's post of GIF on Clinton adds to his history of controversial retweets https://t.co/D3c89c59uJ https://t.co/hCj3om12vN",
  "display_text_range": [
    0,
    100
  ],
  "source": "<a href=\"https://dlvrit.com/\" rel=\"nofollow\">dlvr.it</a>",
  "truncated": false,
  "in_reply_to_status_id": null,
  "in_reply_to_status_id_str": null,
  "in_reply_to_user_id": null,
  "in_reply_to_user_id_str": null,
  "in_reply_to_screen_name": null,
  "user": {
    "id": 799087176218800100,
    "id_str": "799087176218800128",
    "name": "juliodario99",
    "screen_name": "juliodario99",
    "location": "Estados Unidos",
    "url": null,
    "description": "de todo un poco y de poco un todo",
    "translator_type": "none",
    "protected": false,
    "verified": false,
    "followers_count": 8,
    "friends_count": 35,
    "listed_count": 1,
    "favourites_count": 0,
    "statuses_count": 5448,
    "created_at": "Thu Nov 17 03:10:03 +0000 2016",
    "utc_offset": null,
    "time_zone": null,
    "geo_enabled": false,
    "lang": "es",
    "contributors_enabled": false,
    "is_translator": false,
    "profile_background_color": "F5F8FA",
    "profile_background_image_url": "",
    "profile_background_image_url_https": "",
    "profile_background_tile": false,
    "profile_link_color": "1DA1F2",
    "profile_sidebar_border_color": "C0DEED",
    "profile_sidebar_fill_color": "DDEEF6",
    "profile_text_color": "333333",
    "profile_use_background_image": true,
    "profile_image_url": "http://pbs.twimg.com/profile_images/799088689200713728/Cdjh3aLP_normal.jpg",
    "profile_image_url_https": "https://pbs.twimg.com/profile_images/799088689200713728/Cdjh3aLP_normal.jpg",
    "default_profile": true,
    "default_profile_image": false,
    "following": null,
    "follow_request_sent": null,
    "notifications": null
  },
  "geo": null,
  "coordinates": null,
  "place": null,
  "contributors": null,
  "is_quote_status": false,
  "quote_count": 0,
  "reply_count": 0,
  "retweet_count": 0,
  "favorite_count": 0,
  "entities": {
    "hashtags": [],
    "urls": [
      {
        "url": "https://t.co/D3c89c59uJ",
        "expanded_url": "http://dlvr.it/PnsHMf",
        "display_url": "dlvr.it/PnsHMf",
        "indices": [
          77,
          100
        ]
      }
    ],
    "user_mentions": [],
    "symbols": [],
    "media": [
      {
        "id": 909857228013764600,
        "id_str": "909857228013764608",
        "indices": [
          101,
          124
        ],
        "media_url": "http://pbs.twimg.com/media/DKB2VAgVAAAHPJ-.jpg",
        "media_url_https": "https://pbs.twimg.com/media/DKB2VAgVAAAHPJ-.jpg",
        "url": "https://t.co/hCj3om12vN",
        "display_url": "pic.twitter.com/hCj3om12vN",
        "expanded_url": "https://twitter.com/juliodario99/status/909857230668759045/photo/1",
        "type": "photo",
        "sizes": {
          "large": {
            "w": 992,
            "h": 558,
            "resize": "fit"
          },
          "small": {
            "w": 680,
            "h": 383,
            "resize": "fit"
          },
          "medium": {
            "w": 992,
            "h": 558,
            "resize": "fit"
          },
          "thumb": {
            "w": 150,
            "h": 150,
            "resize": "crop"
          }
        }
      }
    ]
  },
  "extended_entities": {
    "media": [
      {
        "id": 909857228013764600,
        "id_str": "909857228013764608",
        "indices": [
          101,
          124
        ],
        "media_url": "http://pbs.twimg.com/media/DKB2VAgVAAAHPJ-.jpg",
        "media_url_https": "https://pbs.twimg.com/media/DKB2VAgVAAAHPJ-.jpg",
        "url": "https://t.co/hCj3om12vN",
        "display_url": "pic.twitter.com/hCj3om12vN",
        "expanded_url": "https://twitter.com/juliodario99/status/909857230668759045/photo/1",
        "type": "photo",
        "sizes": {
          "large": {
            "w": 992,
            "h": 558,
            "resize": "fit"
          },
          "small": {
            "w": 680,
            "h": 383,
            "resize": "fit"
          },
          "medium": {
            "w": 992,
            "h": 558,
            "resize": "fit"
          },
          "thumb": {
            "w": 150,
            "h": 150,
            "resize": "crop"
          }
        }
      }
    ]
  },
  "favorited": false,
  "retweeted": false,
  "possibly_sensitive": false,
  "filter_level": "low",
  "lang": "en",
  "timestamp_ms": "1505761842778"
}
"""
}
