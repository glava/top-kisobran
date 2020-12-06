package org.kisobran.top

import com.typesafe.config.ConfigFactory
import slick.jdbc.{H2Profile, PostgresProfile}

import scala.util.Try

object Configuration {
  val limit = 20
  val currentYear = 2020

  val config = ConfigFactory.load()
  lazy val interface = config.getString("http.interface")
  lazy val port = config.getInt("http.port")
  lazy val dbUrl = Try(config.getString("db.url"))
  lazy val dbUser = Try(config.getString("db.user")).getOrElse("")
  lazy val dbPassword = Try(config.getString("db.password")).getOrElse("")
  lazy val dbProfile = Try(config.getString("db.profile")).map(_ => PostgresProfile).getOrElse(H2Profile)
  lazy val adminPassword = Try(config.getString("admin.password")).getOrElse("p4sswor!d")
}
