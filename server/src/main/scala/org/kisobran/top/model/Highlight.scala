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
    "Glava iz Švabije",
    yt = "https://www.youtube.com/embed/sh5exYGe_Qg",
    "Kada god slušam Cat Power uveren sam da će sve biti ok. Čak i kada je mračno kao u Black pesmi"
  )

  private val milan = Highlight(
    "Midnight Citizen",
    yt = "https://www.youtube.com/embed/4sk0uDbM5lc",
    "Ne postoji bolja pesma za vetar, sunce, oluju, sneg, noć, trčanje, tunel, praznu ulicu, samoću, hangar, bogove. Jon je maestro koji se uvlači pod kožu i izaziva jezu kroz svaku poru."
  )

  private val maki = Highlight(
    "Benjd",
    yt = "https://www.youtube.com/embed/kQtf3oh-2Zg",
    "Ovo je dobra stvar koju možete poslušati ako rešite da odradite kvalitetnu solo šetnjicu sa sluškama u ušima!"
  )
  
  private val mimi = Highlight(
    "Mimi, padavičar iz beograda",
    yt = "https://www.youtube.com/embed/SlbVgjFvE3I",
    "Klupski hit koji možete da prevušite na jeziku koji ne razumete, pa sad dok đuzgate možete pored oponašanja onomatopeje bitova i matrica da izgovarate i ko zna šta na jeziku Dobre Koreje."
  )

   private val brada = Highlight(
    "@pttrn",
    yt = "https://www.youtube.com/embed/kazClFw5Aj0",
    "Novi Lorn, mračan kao tvoja duša."
  )

  val items = Seq(rale, goran, milan, maki, mimi, brada)

  def element() = {
    val random = new Random
    items(random.nextInt(items.size))
  }
}
