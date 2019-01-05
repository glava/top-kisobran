package org.kisobran.top.repository

import org.kisobran.top.db.Stats
import org.kisobran.top.model.Entry

import scala.concurrent.Future

trait StatsRepository {

  def createStats(id: String, artist: String, song: String, position: Int, points: Int): Future[Option[Stats]]

  def createStats(id: String, entries: Seq[Entry]): Future[Seq[Stats]]

  def enable(id: String): Future[Int]

  def select(limit: Int, offset: Int): Future[Seq[Stats]]

  def find(id: String): Future[Seq[Stats]]
}
