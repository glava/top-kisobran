package org.kisobran.top

import java.util.UUID

import akka.http.scaladsl.server.Directives
import org.kisobran.top.model.Entry
import org.kisobran.top.repository.TopListRepository
import org.kisobran.top.shared.SharedMessages
import org.kisobran.top.twirl.Implicits._

import scala.concurrent.ExecutionContext

class WebService(topListRepository: TopListRepository)(implicit executionContext: ExecutionContext) extends Directives {

  val limit = 10

  val route = {
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
              org.kisobran.top.html.lista.render(lista, false)
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
                Entry(formContent(s"inputArtist$index"), formContent(s"inputSong$index"))
              }
              topListRepository.createTopList(formContent.get("userEmail"), entries, formContent.getOrElse("listName", s"untilted-${UUID.randomUUID()}")).map { x =>
                org.kisobran.top.html.lista.render(x, true)
              }
            }
          }
        }
      }
    }
  }
}
