package org.kisobran.top

import java.util.UUID

import akka.http.scaladsl.server.{Directives, Route}
import org.kisobran.top.db.Stats
import org.kisobran.top.model.Entry
import org.kisobran.top.repository.{StatsRepository, TopListRepository}
import org.kisobran.top.shared.SharedMessages
import org.kisobran.top.twirl.Implicits._

import scala.concurrent.{ExecutionContext, Future}

class TopListService(topListRepository: TopListRepository, statsRepository: StatsRepository)(implicit executionContext: ExecutionContext) extends Directives {

  val limit = 10

  val route: Route = {
    pathSingleSlash {
      get {
        complete {
          topListRepository.select(limit, 0).map { all =>
            org.kisobran.top.html.index.render(all, Some(1), None)
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
      pathPrefix("admin" / Remaining) { id =>
        get {
          complete {
            topListRepository.findTopList(id).map { lista =>
              org.kisobran.top.html.lista.render(lista, false, true)
            }
          }
        }
      } ~
      pathPrefix("update") {
        post {
          formFieldMap { formContent =>
            complete {
              topListRepository.update(formContent("id"), formContent.get("yt_link")).flatMap { x =>
                topListRepository.select(limit, 0).map { all =>
                  org.kisobran.top.html.index.render(all, Some(1), None)
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
              topListRepository.select(limit, limit * page).map { all =>
                val backPage = if (page >= 1) Some(page - 1) else None
                val forwardPage = if (count <= (page + 1) * limit) None else Some(page + 1)
                org.kisobran.top.html.index.render(all, forwardPage, backPage)
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

              val entries = (1 to 10).map { index =>
                Entry(formContent(s"inputArtist$index"), formContent(s"inputSong$index"), index, index)
              }
              topListRepository.createTopList(
                formContent.get("userEmail"),
                entries,
                formContent.getOrElse("listName", s"untilted-${UUID.randomUUID()}")
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
