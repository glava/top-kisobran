package org.kisobran.top

import java.nio.file.Files

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.kisobran.top.db.{DbTestConfiguration, SlickTopListRepository}
import org.postgresql.Driver
import slick.jdbc.{DatabaseUrlDataSource, DriverDataSource, H2Profile, PostgresProfile}

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
    val service = new WebService()

    val source = dbUrl.map { url =>
      new DriverDataSource(url, user = dbUser, password = dbPassword, driverClassName = classOf[Driver].getName)
    }.getOrElse(DbTestConfiguration.testMySQL)

    val topListRepository = new SlickTopListRepository(
      source
    )(PostgresProfile, ExecutionContext.global)

    topListRepository.ensureTablesPresent(true)

    Http().bindAndHandle(service.route, interface, port)

    println(s"Server online at http://$interface:$port")
  }
}
