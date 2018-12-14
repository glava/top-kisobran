package org.kisobran.top.repository
import org.kisobran.top.model.{Entry, TopList}
import org.kisobran.top.slick.TopListEntries

import scala.concurrent.Future

trait TopListRepository {

  def createTopList(userEmail: String, entries: Seq[Entry], listName: String): Future[Option[TopListEntries]]
  def getTopList(id: String): Future[Option[TopListEntries]]

}
