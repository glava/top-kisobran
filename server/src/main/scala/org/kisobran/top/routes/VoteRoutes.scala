package org.kisobran.top.routes

import java.util.UUID

import akka.http.scaladsl.server.{Directives, Route}
import org.kisobran.top.Configuration
import org.kisobran.top.db.Stats
import org.kisobran.top.model.Entry
import org.kisobran.top.repository.{StatsRepository, TopListRepository}
import org.kisobran.top.util.LoggingSupport
import org.kisobran.top.twirl.Implicits._
import play.twirl.api.HtmlFormat

import scala.concurrent.{ExecutionContext, Future}

class VoteRoutes(topListRepository: TopListRepository,
                 statsRepository: StatsRepository)(implicit executionContext: ExecutionContext) extends Directives with LoggingSupport {

  val route: Route = pathPrefix("vote") {
    post(formFieldMap(formContent => complete(votePage(formContent))))
  }

  private def votePage(formContent: Map[String, String]): Future[Future[HtmlFormat.Appendable]] = {
    val title = formContent.getOrElse("listName", s"untilted-${UUID.randomUUID()}")
    val entries = (1 to 10).map { index =>
      Entry(formContent(s"inputArtist$index"), formContent(s"inputSong$index"), index, index)
    }

    log.info(title)
    log.info(entries.toString)

    topListRepository.createTopList(
      formContent.get("userEmail"),
      entries,
      title,
      enabled = false,
      Configuration.currentYear
    ).map { topListEntry =>
      val insertOperation: Future[Seq[Stats]] = topListEntry.map { topList =>
        statsRepository.createStats(topList.id, entries)
      }.getOrElse(Future.successful(Seq.empty))

      insertOperation.map { _ =>
        org.kisobran.top.html.lista.render(topListEntry, message = true, admin = false)
      }
    }
  }
}
