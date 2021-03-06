package org.kisobran.top.db

import scala.util.Try

object EmbeddedUtil {

  def toEmbedded(embeddedLinkFromWeb: String): String = {
    if (embeddedLinkFromWeb.contains("soundcloud")) {
      // sc links are just passed because it can be cleaned from crap
      s"${embeddedLinkFromWeb}&color=%23ff5500&auto_play=false&hide_related=false&show_comments=true&show_user=true&show_reposts=false&show_teaser=true&visual=true"
    } else {
      s"https://www.youtube.com/embed/${embeddedLinkFromWeb.split("/").last}"
    }
  }

  def spotify(spotifyPLaylist: String): Option[String] = {
    Try {
      "https://open.spotify.com/embed/playlist/" + spotifyPLaylist.split("playlist/").tail.head
    }.toOption
  }
}
