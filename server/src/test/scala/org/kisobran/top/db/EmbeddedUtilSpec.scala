package org.kisobran.top.db

import org.scalatest.{FlatSpec, Matchers}

class EmbeddedUtilSpec extends FlatSpec with Matchers {
  import EmbeddedUtil._
  "YTUtil" should "return embeddable link version" in {
    toEmbedded("https://youtu.be/F0eoeLch4xM") should be("https://www.youtube.com/embed/F0eoeLch4xM")
  }

  "it" should "process soundcloud links" in {
    toEmbedded("https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/tracks/323905173") should be("https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/tracks/323905173&color=%23ff5500&auto_play=false&hide_related=false&show_comments=true&show_user=true&show_reposts=false&show_teaser=true&visual=true")
  }
}
