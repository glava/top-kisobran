package org.kisobran.top

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.kisobran.top.db.{DbTestConfiguration, SlickTopListRepository}
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

    // todo: move to liqubase
    if (dbProfile == H2Profile)
      topListRepository.ensureTablesPresent(true)
    else
      topListRepository.ensureTablesPresent(true)

    val service = new WebService(topListRepository)(ExecutionContext.global)
    Http().bindAndHandle(service.route, interface, port)

    println(s"Server online at http://$interface:$port")
  }
}
