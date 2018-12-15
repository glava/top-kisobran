package org.kisobran.top.repository
import org.kisobran.top.model.{Entry, TopList}
import org.kisobran.top.db.TopListEntries

import scala.concurrent.Future

trait TopListRepository {

  def createTopList(userEmail: Option[String], entries: Seq[Entry], listName: String): Future[Option[TopListEntries]]
  def findTopList(id: String): Future[Option[TopListEntries]]
  def select(limit: Int, offset: Int): Future[Seq[TopListEntries]]
  def count(): Future[Int]

}
