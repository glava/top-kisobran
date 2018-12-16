package org.kisobran.top.repository
import org.kisobran.top.db.TopListEntries
import org.kisobran.top.model.Entry

import scala.concurrent.Future

trait TopListRepository {

  def createTopList(userEmail: Option[String], entries: Seq[Entry], listName: String, enabled: Boolean = false): Future[Option[TopListEntries]]

  def findTopList(id: String): Future[Option[TopListEntries]]

  def select(limit: Int, offset: Int, isEnabled: Boolean): Future[Seq[TopListEntries]]

  def count(): Future[Int]

  def update(id: String, ytLink: Option[String]): Future[Int]
}
