package org.kisobran.top.model


case class TopList(id: Long,
                   entries: Seq[Entry],
                   userEmail: String,
                   year: Int,
                   title: String,
                   created: Long)

case class Entry(artist: String, song: String, position: Int, points: Int)


