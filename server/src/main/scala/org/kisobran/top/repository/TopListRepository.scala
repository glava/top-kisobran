package org.kisobran.top.repository
import org.kisobran.top.db.TopListEntries
import org.kisobran.top.model.Entry

import scala.concurrent.Future

trait TopListRepository {

  def createTopList(userEmail: Option[String],
                    entries: Seq[Entry],
                    listName: String,
                    enabled: Boolean = false,
                    year: Int,
                    ytLink: Option[String] = None
                   ): Future[Option[TopListEntries]]

  def findTopList(id: String): Future[Option[TopListEntries]]

  def findTopList(ids: Seq[String]): Future[Seq[TopListEntries]]

  def select(limit: Int, offset: Int, isEnabled: Boolean, year:Int): Future[Seq[TopListEntries]]

  def count(year: Int): Future[Int]

  def update(id: String, ytLink: Option[String]): Future[Int]
}
