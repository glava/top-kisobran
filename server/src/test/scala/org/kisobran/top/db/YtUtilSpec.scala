package org.kisobran.top.db

import org.scalatest.{FlatSpec, Matchers}

class YtUtilSpec extends FlatSpec with Matchers with YtUtil {


  "YTUtil" should "return embeddable link version" in {
    toEmbedded("https://youtu.be/F0eoeLch4xM") should be("https://www.youtube.com/embed/F0eoeLch4xM")
  }

}
