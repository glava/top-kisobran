package org.kisobran.top

import java.util.UUID

import akka.http.scaladsl.server.directives.Credentials
import akka.http.scaladsl.server.{Directives, Route}
import org.kisobran.top.db.{Stats, TopListEntries}
import org.kisobran.top.model.Entry
import org.kisobran.top.repository.{StatsRepository, TopListRepository}
import org.kisobran.top.shared.SharedMessages
import org.kisobran.top.twirl.Implicits._
import Configuration._
import com.github.blemale.scaffeine
import com.github.blemale.scaffeine.Scaffeine
import org.kisobran.top.model.Highlight._
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

class TopListService(topListRepository: TopListRepository, statsRepository: StatsRepository)(implicit executionContext: ExecutionContext) extends Directives {

  val selectCache: scaffeine.AsyncLoadingCache[(Int, Int, Boolean, Int), Seq[TopListEntries]] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(40.minutes)
      .maximumSize(100)
      .buildAsyncFuture[(Int, Int, Boolean, Int), Seq[TopListEntries]] {
      case (limit, offset, isEnabled, year) => topListRepository.select(limit, offset, isEnabled, year)
    }

  val limit = 10
  val currentYear = 2018

  def myUserPassAuthenticator(credentials: Credentials): Option[String] =
    credentials match {
      case p@Credentials.Provided(id) if p.verify(adminPassword) => Some(id)
      case _ => None
    }

  val route: Route = {
    pathSingleSlash {
      get {
        complete {
          selectCache.get(20, 0, true, currentYear).map { all =>
            org.kisobran.top.html.index.render(all, None, None, element())
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
              org.kisobran.top.html.lista.render(lista, false, false)
            }
          }
        }
      } ~
      pathPrefix("admin") {
        authenticateBasic(realm = "secure site", myUserPassAuthenticator) { _ =>
          parameters('id ?, 'year ?) { (maybeId, maybeYear) =>
            get {
              complete {
                maybeId match {
                  case Some(id) =>
                    topListRepository.findTopList(id).map { lista =>
                      org.kisobran.top.html.lista.render(lista, false, true)
                    }
                  case None =>
                    selectCache.get(100, 0, false, maybeYear.map(_.toInt).getOrElse(currentYear)).map { all =>
                      org.kisobran.top.html.admin.render(all)
                    }
                }


              }
            }
          }
        }
      } ~
      pathPrefix("update") {
        post {
          formFieldMap { formContent =>
            complete {
              topListRepository.update(formContent("id"), formContent.get("yt_link")).flatMap { x =>
                selectCache.get(limit, 0, true, currentYear).map { all =>
                  org.kisobran.top.html.index.render(all, Some(1), None, element())
                }
              }
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
                org.kisobran.top.html.lista.render(t.head, false, false)
              }
            }
          }
        }
      } ~
      pathPrefix("arhiva") {
        parameters('year) { _year =>
          parameters('page ?) { pageS =>
            complete {
              val page = pageS.map(_.toInt).getOrElse(0)
              topListRepository.count(_year.toInt).flatMap { count =>
                selectCache.get(limit, limit * page, true, _year.toInt).map { all =>
                  val backPage = if (page >= 1) Some(page - 1) else None
                  val forwardPage = if (count <= (page + 1) * limit) None else Some(page + 1)
                  org.kisobran.top.html.arhiva.render(all, forwardPage, backPage, _year.toInt)
                }
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
      pathPrefix("assets" / Remaining) { file =>
        // optionally compresses the response with Gzip or Deflate
        // if the client accepts compressed responses
        encodeResponse {
          getFromResource("public/" + file)
        }
      } ~ {
      pathPrefix("vote") {
        post {
          formFieldMap { formContent =>
            complete {
              val title = formContent.getOrElse("listName", s"untilted-${UUID.randomUUID()}")
              val entries = (1 to 10).map { index =>
                Entry(formContent(s"inputArtist$index"), formContent(s"inputSong$index"), index, index)
              }

              println(title)
              println(entries)

              topListRepository.createTopList(
                formContent.get("userEmail"),
                entries,
                title,
                false,
                2018
              ).map { topListEntry =>
                val insertOperation: Future[Seq[Stats]] = topListEntry.map { topList =>
                  statsRepository.createStats(topList.id, entries)
                }.getOrElse(Future.successful(Seq.empty))

                insertOperation.map { _ =>
                  org.kisobran.top.html.lista.render(topListEntry, true, false)
                }
              }
            }
          }
        }
      }
    }
  }
}
