package org.kisobran.top.routes

import akka.http.scaladsl.server.{Directives, Route}
import akka.http.scaladsl.server.directives.Credentials
import org.kisobran.top.Configuration
import org.kisobran.top.repository.{StatsRepository, TopListRepository}

import scala.concurrent.{ExecutionContext, Future}
import org.kisobran.top.twirl.Implicits._
import play.twirl.api.HtmlFormat

class AdminRoutes(authenticator: Credentials => Option[String],
                  topListRepository: TopListRepository,
                  statsRepository: StatsRepository)(implicit executionContext: ExecutionContext) extends Directives {
  val route: Route = pathPrefix("admin") {
    authenticateBasic(realm = "secure site", authenticator) { _ =>
      parameters('id ?, 'year ?) { (maybeId, maybeYear) =>
        get(complete(adminPage(maybeId, maybeYear)))
      }
    }
  }

  def adminPage(maybeId: Option[String], maybeYear: Option[String]): Future[HtmlFormat.Appendable] = {
    maybeId match {
      case Some(id) =>
        topListRepository.findTopList(id).flatMap { lista =>
          statsRepository.find(id).map { stats =>
            org.kisobran.top.html.lista.render(lista, false, true, stats, Seq.empty)
          }
        }
      case None =>
        topListRepository.select(
          limit = 100,
          offset = 0,
          isEnabled = false,
          maybeYear.map(_.toInt).getOrElse(Configuration.currentYear)
        ).map { all =>
          org.kisobran.top.html.admin.render(all)
        }
    }
  }
}
