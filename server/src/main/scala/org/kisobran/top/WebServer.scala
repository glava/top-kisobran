package org.kisobran.top

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.kisobran.top.db.{DbTestConfiguration, SlickStatsRepository, SlickTopListRepository}
import org.kisobran.top.model.Entry
import org.postgresql.Driver
import slick.jdbc.{DriverDataSource, H2Profile, PostgresProfile}

import scala.concurrent.ExecutionContext
import scala.util.Try

object WebServer {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("server-system")
    implicit val materializer = ActorMaterializer()

    val config = ConfigFactory.load()
    val interface = config.getString("http.interface")
    val port = config.getInt("http.port")
    val dbUrl = Try(config.getString("db.url"))
    val dbUser = Try(config.getString("db.user")).getOrElse("")
    val dbPassword = Try(config.getString("db.password")).getOrElse("")
    val dbProfile = Try(config.getString("db.profile")).map(_ => PostgresProfile).getOrElse(H2Profile)

    val source = dbUrl.map { url =>
      new DriverDataSource(url, user = dbUser, password = dbPassword, driverClassName = classOf[Driver].getName)
    }.getOrElse(DbTestConfiguration.testMySQL)

    val topListRepository = new SlickTopListRepository(source)(dbProfile, ExecutionContext.global)
    implicit val ex = ExecutionContext.global
    // todo: move to liqubase
    if (dbProfile == H2Profile) {
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
    else
      topListRepository.ensureTablesPresent(true)

    val statsRepository = new SlickStatsRepository(source)(dbProfile, ExecutionContext.global)

    val service = new TopListService(topListRepository, statsRepository)(ExecutionContext.global)
    Http().bindAndHandle(service.route, interface, port)

    println(s"Server online at http://$interface:$port")
  }
}
