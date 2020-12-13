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
    yt = EmbeddedUtil.toEmbedded("https://www.youtube.com/embed/hp0KiJs0S5g"),
    "Cleo Sol je sve što mi je trebalo ove godine"
  )

//  private val vidak = Highlight(
//    "Vidak",
//    yt = EmbeddedUtil.toEmbedded("https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/tracks/590543667"),
//    "Ja sam Vidak ide mi se u džihad."
//  )

  private val milan = Highlight(
    "Midnight Citizen",
    yt = "https://www.youtube.com/embed/qS_o7XpnEqA",
    "2020. godina nam je po mnogo čemu ostala dužna, ali kada se sve vrati u neku vrstu normale, prva pesma za plesni podijum će nesumnjivo biti pobednička himna Jessie Ware."
  )

  private val maki = Highlight(
    "Benjd",
    yt = "https://www.youtube.com/embed/kQtf3oh-2Zg",
    "Ovo je dobra stvar koju možete poslušati ako rešite da odradite kvalitetnu solo šetnjicu sa sluškama u ušima!"
  )

  private val kimi = Highlight(
    "Kimi, idejni tvorac top deset akcije",
    yt = EmbeddedUtil.toEmbedded("https://www.youtube.com/embed/lo4KMGiy--Y"),
    "Bar je za muziku bila dobra godina"
  )
  
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

  val items = Seq(mimi, whisper, milan, goran, kimi)

  def element() = {
    val random = new Random
    items(random.nextInt(items.size))
  }
}
