package org.kisobran.top

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.kisobran.top.db.{DbTestConfiguration, SlickStatsRepository, SlickTopListRepository}
import org.kisobran.top.model.Entry
import org.postgresql.Driver
import slick.jdbc.{DriverDataSource, H2Profile}

import scala.concurrent.ExecutionContext

object WebServer {
  def main(args: Array[String]) {
    import Configuration._

    implicit val system = ActorSystem("server-system")
    implicit val materializer = ActorMaterializer()

    val source = dbUrl.map { url =>
      new DriverDataSource(url, user = dbUser, password = dbPassword, driverClassName = classOf[Driver].getName)
    }.getOrElse(DbTestConfiguration.testMySQL)

    val topListRepository = new SlickTopListRepository(source)(dbProfile, ExecutionContext.global)
    val statsRepository = new SlickStatsRepository(source)(dbProfile, ExecutionContext.global)

    implicit val ex = ExecutionContext.global
    // todo: move to liqubase
    if (dbProfile == H2Profile) {
      statsRepository.ensureTablesPresent(true)
      topListRepository.ensureTablesPresent(true).map { _ =>
        (1 to 20).map { t =>
          topListRepository.createTopList(Some("use@somebody.com"),
          (1 to 10).map { i => Entry(s"artist${i}", s"song${i}", i, i) },
          s"${t}-best-list",
          false
          )
        }
      }
    }
    else {
      statsRepository.ensureTablesPresent(true)
      topListRepository.ensureTablesPresent(true)
    }


    val service = new TopListService(topListRepository, statsRepository)(ExecutionContext.global)
    Http().bindAndHandle(service.route, interface, port)

    println(s"Server online at http://$interface:$port")
  }
}
