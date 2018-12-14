package org.kisobran.top

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

object WebServer {
  def main(args: Array[String]) {
    implicit val system = ActorSystem("server-system")
    implicit val materializer = ActorMaterializer()

    val config = ConfigFactory.load()
    val interface = config.getString("http.interface")
    val port = config.getInt("http.port")
    val db = config.getString("db.url")
    println(db)

    println("1")
    def propOrEnv(key: String): Option[String] = sys.props.get(key).orElse(sys.env.get(key))
    println(propOrEnv("db.url"))
    println(propOrEnv("DATABASE_URL"))
    val service = new WebService()

    Http().bindAndHandle(service.route, interface, port)

    println(s"Server online at http://$interface:$port")
  }
}
