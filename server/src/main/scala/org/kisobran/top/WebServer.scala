package org.kisobran.top

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.kisobran.top.db.{DbTestConfiguration, EmbeddedUtil, SlickStatsRepository, SlickTopListRepository}
import org.kisobran.top.model.Entry
import org.kisobran.top.repository.InMemoryWinnersRepository
import org.kisobran.top.util.LoggingSupport
import org.postgresql.Driver
import slick.jdbc.{DriverDataSource, H2Profile}

import scala.concurrent.ExecutionContext

object WebServer extends LoggingSupport {
  def main(args: Array[String]) {
    import Configuration._

    implicit val system = ActorSystem("server-system")
    implicit val materializer = ActorMaterializer()

    val source = dbUrl.map { url =>
      new DriverDataSource(url, user = dbUser, password = dbPassword, driverClassName = classOf[Driver].getName)
    }.getOrElse(DbTestConfiguration.testMySQL)

    val topListRepository = new SlickTopListRepository(source)(dbProfile, ExecutionContext.global)
    val statsRepository = new SlickStatsRepository(source)(dbProfile, ExecutionContext.global)
    val winnersRepository = InMemoryWinnersRepository

    implicit val ex = ExecutionContext.global
    // todo: move to liqubase
    if (dbProfile == H2Profile) {

      val videos: Seq[String] = Seq(
        EmbeddedUtil.toEmbedded("https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/tracks/323905173"), // goran series
        EmbeddedUtil.spotify("https://open.spotify.com/playlist/6NrOhAuNMMG3cHBJ1kAm78").get,
        EmbeddedUtil.toEmbedded("https://youtu.be/BwSGubat4hY") // worst darts
      )

      statsRepository.ensureTablesPresent(true).map { _ =>
        topListRepository.ensureTablesPresent(true).map { _ =>
          (1 to 20).map { t =>
            val entries = (1 to 10).map { i => Entry(s"artist${i}", s"song${i}", i, i) }
            topListRepository.createTopList(
              userEmail = Some("use@somebody.com"),
              entries,
              s"${t}-best-list",
              true,
              Configuration.currentYear,
              Some(videos(t % videos.size) + s"?$t")
            ).map { entrie =>
              entrie.map { lists =>
                statsRepository.createStats(lists.id, entries)
              }
            }
          }
        }
      }
    }
    else {
      statsRepository.ensureTablesPresent(false)
      topListRepository.ensureTablesPresent(false)
    }


    val service = new TopListService(topListRepository, statsRepository, winnersRepository)(ExecutionContext.global)
    Http().bindAndHandle(service.route, interface, port)

    log.info(s"Server online at http://$interface:$port")
  }
}
