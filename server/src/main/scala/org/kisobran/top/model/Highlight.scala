package org.kisobran.top.model

import org.kisobran.top.db.EmbeddedUtil

import scala.util.Random

case class Highlight(author: String, yt: String, desc: String)


object Highlight {

  private val rale = Highlight(
    "Rale Kišobran",
    yt = EmbeddedUtil.toEmbedded("https://youtu.be/5FQtSn_vak0"),
    "You were waiting outside for me in the sun."
  )

  private val goran = Highlight(
    "Vincent Van Gogo",
    yt = EmbeddedUtil.toEmbedded("https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/tracks/616622964"),
    "SAULT jer se i dalje ložim ko tinejdžer na mistične i drske bendove"
  )

//  private val vidak = Highlight(
//    "Vidak",
//    yt = EmbeddedUtil.toEmbedded("https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/tracks/590543667"),
//    "Ja sam Vidak ide mi se u džihad."
//  )

//  private val milan = Highlight(
//    "Midnight Citizen",
//    yt = "https://www.youtube.com/embed/4sk0uDbM5lc",
//    "Ne postoji bolja pesma za vetar, sunce, oluju, sneg, noć, trčanje, tunel, praznu ulicu, samoću, hangar, bogove. Jon je maestro koji se uvlači pod kožu i izaziva jezu kroz svaku poru."
//  )

  private val maki = Highlight(
    "Benjd",
    yt = "https://www.youtube.com/embed/kQtf3oh-2Zg",
    "Ovo je dobra stvar koju možete poslušati ako rešite da odradite kvalitetnu solo šetnjicu sa sluškama u ušima!"
  )

//  private val kimi = Highlight(
//    "Kimi, idejni tvorac top deset akcije",
//    yt = EmbeddedUtil.toEmbedded("https://youtu.be/mMDeJfJttUU"),
//    "Samo elektronika i sajentologija"
//  )
  
  private val mimi = Highlight(
    "Mimi, padavičar iz beograda",
    yt = "https://www.youtube.com/embed/_Khh8NB-qBo",
    "Nikad više muzike za đuskanje a nikad manje vikenada za izlaske."
  )

//   private val brada = Highlight(
//    "@pttrn",
//    yt = EmbeddedUtil.toEmbedded("https://youtu.be/36Ra3vTMSo0"),
//    "Droppin hits like Giannis."
//  )
   
//   private val fica = Highlight(
//    "dothemath",
//    yt = "https://www.youtube.com/embed/gk7W-QowBss",
//    "2019. je pokazala da je Jme bolji brat."
//  )

  private val whisper = Highlight(
    "Whisper",
    yt = "https://www.youtube.com/embed/-dfkW9OLlFM",
    "Malo ko je u stanju da peva o ovim stvarima u 2020 a da to ne zvuči otrcano.\nThe Magnetic Fields jesu. "
  )

  val items = Seq(mimi, whisper)

  def element() = {
    val random = new Random
    items(random.nextInt(items.size))
  }
}
