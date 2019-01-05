package org.kisobran.top

import akka.http.scaladsl.server.directives.Credentials
import akka.http.scaladsl.server.{Directives, Route}
import com.github.blemale.scaffeine
import com.github.blemale.scaffeine.Scaffeine
import org.kisobran.top.Configuration._
import org.kisobran.top.db.{TopListEntries, YtUtil}
import org.kisobran.top.model.Highlight._
import org.kisobran.top.repository.{StatsRepository, TopListRepository, WinnersRepository}
import org.kisobran.top.routes.{AdminRoutes, ArchiveRoutes, RecommendationRoutes, VoteRoutes}
import org.kisobran.top.shared.SharedMessages
import org.kisobran.top.twirl.Implicits._
import org.kisobran.top.util.LoggingSupport

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

object TopListService {
  type EntryCache = scaffeine.AsyncLoadingCache[(Int, Int, Boolean, Int), Seq[TopListEntries]]
}

class TopListService(topListRepository: TopListRepository,
                     statsRepository: StatsRepository,
                     winnerRepository: WinnersRepository)
                    (implicit executionContext: ExecutionContext)
  extends Directives with YtUtil with LoggingSupport {

  val selectCache: TopListService.EntryCache =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(40.minutes)
      .maximumSize(100)
      .buildAsyncFuture[(Int, Int, Boolean, Int), Seq[TopListEntries]] {
      case (limit, offset, isEnabled, year) => topListRepository.select(limit, offset, isEnabled, year)
    }

  lazy val myUserPassAuthenticator: Credentials => Option[String] = {
    case p@Credentials.Provided(id) if p.verify(adminPassword) => Some(id)
    case _ => None
  }

  lazy val adminRoutes = new AdminRoutes(myUserPassAuthenticator, topListRepository, statsRepository)
  lazy val archiveRoutes = new ArchiveRoutes(topListRepository, selectCache)
  lazy val voteRoutes = new VoteRoutes(topListRepository, statsRepository)
  lazy val recommendationRoutes = new RecommendationRoutes()

  val route: Route = {
    pathSingleSlash {
      get {
        complete {
          selectCache.get(limit, 0, true, currentYear).map { all =>
            org.kisobran.top.html.index.render(all, Some(1), None, element())
          }
        }
      }
    } ~
      pathPrefix("glasaj") {
        get {
          complete {
            org.kisobran.top.html.glasaj.render(SharedMessages.itWorks)
          }
        }
      } ~
      pathPrefix("lista" / Remaining) { id =>
        get {
          complete {
            topListRepository.findTopList(id).map { lista =>
              org.kisobran.top.html.lista.render(lista, false, false, Seq.empty)
            }
          }
        }
      } ~
      archiveRoutes.route ~
      adminRoutes.route ~
      voteRoutes.route ~
      recommendationRoutes.route ~
      pathPrefix("update") {
        post {
          formFieldMap { formContent =>
            complete {
              for {
                _ <- topListRepository.update(formContent("id"), formContent.get("yt_link").map(toEmbedded))
                _ <- statsRepository.enable(formContent("id"))
                all <- selectCache.get(limit, 0, true, currentYear)
              } yield org.kisobran.top.html.index.render(all, Some(1), None, element())
            }
          }
        }
      } ~
      pathPrefix("pravila") {
        get {
          complete {
            org.kisobran.top.html.pravila.render()
          }
        }
      } ~
      pathPrefix("import") {
        authenticateBasic(realm = "secure site", myUserPassAuthenticator) { _ =>
          parameters('year, 'page, 'yt ?, 'skip ?) { (_year, _page, yt, skip) =>
            complete {
              val page = _page.toInt
              val year = _year.toInt
              Crawler.extract(year, skip.isDefined, yt.isDefined, page)(topListRepository).map { t =>
                org.kisobran.top.html.lista.render(t.head, false, false, Seq.empty)
              }
            }
          }
        }
      } ~
      pathPrefix("liste") {
        parameters('page) { pageS =>
          complete {
            val page = pageS.toInt
            topListRepository.count().flatMap { count =>
              selectCache.get(limit, limit * page, true, 2018).map { all =>
                val backPage = if (page >= 1) Some(page - 1) else None
                val forwardPage = if (count <= (page + 1) * limit) None else Some(page + 1)
                org.kisobran.top.html.index.render(all, forwardPage, backPage, element())
              }
            }
          }
        }
      } ~
      pathPrefix("pobednici" / Remaining) { year =>
          complete {
            winnerRepository.winners(Some(year.toInt)).map { winners =>
              org.kisobran.top.html.winners.render(winners)
            }
        }
      } ~
      pathPrefix("pobednici") {
        complete {
          winnerRepository.winners(None).map { winners =>
            org.kisobran.top.html.winners.render(winners)
          }
        }
      } ~
      pathPrefix("assets" / Remaining) { file =>
        // optionally compresses the response with Gzip or Deflate
        // if the client accepts compressed responses
        encodeResponse {
          getFromResource("public/" + file)
        }
      }

  }
}
