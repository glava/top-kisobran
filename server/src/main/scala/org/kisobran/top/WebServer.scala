package org.kisobran.top

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import org.kisobran.top.db.SlickTopListRepository
import org.postgresql.Driver
import slick.jdbc.{DatabaseUrlDataSource, DriverDataSource, H2Profile, PostgresProfile}

import scala.concurrent.ExecutionContext


object WebServer {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("server-system")
    implicit val materializer = ActorMaterializer()

    val config = ConfigFactory.load()
    val interface = config.getString("http.interface")
    val port = config.getInt("http.port")
    val dbUrl = config.getString("db.url")
    val dbUser = config.getString("db.user")
    val dbPassword = config.getString("db.password")
    val service = new WebService()

    val source = new DriverDataSource(dbUrl, user = dbUser, password = dbPassword, driverClassName = classOf[Driver].getName)
    val topListRepository = new SlickTopListRepository(
      source
    )(PostgresProfile, ExecutionContext.global)

    topListRepository.ensureTablesPresent(true)

    Http().bindAndHandle(service.route, interface, port)

    println(s"Server online at http://$interface:$port")
  }
}
