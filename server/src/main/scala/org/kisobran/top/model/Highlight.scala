package org.kisobran.top.model

import scala.util.Random

case class Highlight(author: String, yt: String, desc: String)


object Highlight {

  private val rale = Highlight(
    "Rale iz Kišobran DJ ekipe",
    yt = "https://www.youtube.com/embed/IJrKlSkxRHA",
    "Feelgood himna koju pakujem u kofer pred put. Pesma sa kojom sam dolazio i odlazio. Sve je cool."
  )

  private val goran = Highlight(
    "Glava",
    yt = "https://www.youtube.com/embed/sh5exYGe_Qg",
    "Kada god slušam Cat Power uveren sam da će sve biti ok. Čak kada je mračno kao u Black pesmi"
  )
  val items = Seq(rale, goran)

  def element() = {
    val random = new Random
    items(random.nextInt(items.size))
  }
}
