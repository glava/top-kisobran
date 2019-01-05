package org.kisobran.top.db

import java.time.{LocalDateTime, ZoneOffset}

import javax.sql.DataSource
import org.kisobran.top.model.Entry
import org.kisobran.top.repository.StatsRepository
import shapeless._
import slick.jdbc.JdbcProfile
import slick.lifted
import slickless._

import scala.concurrent.{ExecutionContext, Future}

class SlickStatsRepository(dataSource: DataSource)(implicit val profile: JdbcProfile,
                                                     implicit val executionContext: ExecutionContext)
  extends StatsRepository with BaseRepo {

  import profile.api._

  val db: profile.backend.DatabaseDef = profile.backend.Database.forDataSource(dataSource, Some(1))

  class StatsTable(tag: Tag) extends Table[Stats](tag, "STATS") {
    def id = column[String]("ID")

    def position = column[Int]("POSITION")

    def song = column[String]("SONG")

    def artist = column[String]("ARTIST")

    def points = column[Int]("POINTS")

    def enabled = column[Boolean]("ENABLED")

    def updatedAt = column[Long]("UPDATED_AT")

    def * = (id :: position :: song :: artist :: points :: enabled :: updatedAt :: HNil).mappedWith(Generic[Stats])
  }

  val statsTable = lifted.TableQuery[StatsTable]

  override def tables: Seq[SomeTable] = Seq(statsTable.asInstanceOf[SomeTable])

  override def select(limit: Int, offset: Int): Future[Seq[Stats]] = {
    db.run(statsTable
      .sortBy(_.updatedAt.desc)
      .drop(offset)
      .take(limit)
      .result
    )
  }

  override def createStats(id: String, artist: String, song: String, position: Int, points: Int): Future[Option[Stats]] = {
    val row = Stats(
    updatedAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
    enabled = false,
    id = id,
    song = song,
    artist = artist,
    position = position,
    points = points
    )

    db.run(statsTable += row).map {
      case 1 => Some(row)
      case _ => None
    }
  }

  override def enable(id: String): Future[Int] = {
    val en = for {list <- statsTable.filter(_.id === id)} yield (list.enabled, list.updatedAt)
    db.run(en.update(true, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)))
  }

  override def createStats(id: String, entries: Seq[Entry]): Future[Seq[Stats]] = {
    val bulkStats = entries.map(entry =>
      Stats(id, entry.position, entry.song, entry.artist, entry.points, false, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
    )

    db.run(statsTable ++= bulkStats).map(_ => bulkStats)
  }

  override def find(id: String): Future[Seq[Stats]] = {
    db.run(statsTable.filter(_.id === id).take(10).result)
  }
}

final case class Stats(id: String, position: Int, song: String, artist: String, points: Int, enabled: Boolean, updatedAt: Long)
