package org.kisobran.top.routes

import java.util.UUID

import akka.http.scaladsl.server.{Directives, Route}
import org.kisobran.top.Configuration
import org.kisobran.top.db.{EmbeddedUtil, Stats}
import org.kisobran.top.model.Entry
import org.kisobran.top.repository.{StatsRepository, TopListRepository}
import org.kisobran.top.util.LoggingSupport
import org.kisobran.top.twirl.Implicits._
import play.twirl.api.HtmlFormat

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}

class VoteRoutes(topListRepository: TopListRepository,
                 statsRepository: StatsRepository)(implicit executionContext: ExecutionContext) extends Directives with LoggingSupport {

  case class VoteForm(title: String, entries: Seq[Entry], email: Option[String], externalPlaylist: Option[String])

  def parse(formContent: Map[String, String]): Option[VoteForm] = {
    val title: String = formContent.getOrElse("listName", s"untilted-${UUID.randomUUID()}")
    val externalPlaylist: Option[String] = formContent.get("externalPlaylist")
    val entries: immutable.IndexedSeq[Entry] = (1 to 10).map {
      case 1 => Entry(formContent(s"inputArtist1"), formContent(s"inputSong1"), position = 1, points = 10)
      case 2 => Entry(formContent(s"inputArtist2"), formContent(s"inputSong2"), position = 2, points = 9)
      case index@_ => Entry(formContent(s"inputArtist$index"), formContent(s"inputSong$index"), position = index, points = 8)
    }
    if (entries.exists(e => e.artist.contains("""http://""") || e.song.contains("http://") || e.song.contains("https://") || e.artist.contains("https://"))) {
      None
    } else Some(VoteForm(title, entries, formContent.get("userEmail"), externalPlaylist))
  }

  val route: Route = pathPrefix("vote") {
    post(formFieldMap(
      formContent => {
        val page: Future[Future[HtmlFormat.Appendable]] = parse(formContent).map(votePage).getOrElse(
          Future.successful(Future.successful(org.kisobran.top.html.voted.render()))
        )
        complete(page)
      }
    ))
  }

  private def votePage(voteForm: VoteForm): Future[Future[HtmlFormat.Appendable]] = {
    log.info(voteForm.title)
    log.info(voteForm.entries.toString)

    topListRepository.createTopList(
      voteForm.email,
      voteForm.entries,
      voteForm.title,
      enabled = false,
      Configuration.currentYear,
      ytLink = voteForm.externalPlaylist.flatMap(p => EmbeddedUtil.embeddedSpotify(p))
    ).map { topListEntry =>
      val insertOperation: Future[Seq[Stats]] = topListEntry.map { topList =>
        statsRepository.createStats(topList.id, voteForm.entries)
      }.getOrElse(Future.successful(Seq.empty))
      insertOperation.map { _ =>
        org.kisobran.top.html.lista.render(topListEntry, message = true, admin = false, Seq.empty, Seq.empty)
      }
    }
  }
}
