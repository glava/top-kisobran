package org.kisobran.top.routes

import akka.http.scaladsl.server.{Directives, Route}
import org.kisobran.top.Configuration.limit
import org.kisobran.top.TopListService
import org.kisobran.top.repository.TopListRepository

import scala.concurrent.{ExecutionContext, Future}
import org.kisobran.top.twirl.Implicits._
import play.twirl.api.HtmlFormat

class ArchiveRoutes(topListRepository: TopListRepository,
                    cache: TopListService.EntryCache)(implicit executionContext: ExecutionContext) extends Directives {
  val route: Route = pathPrefix("arhiva") {
    parameters('year) { _year =>
      parameters('page ?) { pageS =>
        complete(archivePage(pageS, _year))
      }
    }
  }

  def archivePage(pageS: Option[String], _year: String): Future[HtmlFormat.Appendable] = {
    val page = pageS.map(_.toInt).getOrElse(0)
    topListRepository.count(_year.toInt).flatMap { count =>
      cache.get(limit, limit * page, true, _year.toInt).map { all =>
        val backPage = if (page >= 1) Some(page - 1) else None
        val forwardPage = if (count <= (page + 1) * limit) None else Some(page + 1)
        org.kisobran.top.html.arhiva.render(all, forwardPage, backPage, _year.toInt)
      }
    }
  }

}
