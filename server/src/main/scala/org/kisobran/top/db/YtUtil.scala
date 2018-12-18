package org.kisobran.top.db

trait YtUtil {

  def toEmbedded(ytLinkFromWeb: String): String = {
   s"https://www.youtube.com/embed/${ytLinkFromWeb.split("/").last}"
  }

}
