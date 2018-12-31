package org.kisobran.top.db

import java.time.{LocalDateTime, ZoneOffset}
import java.util.UUID

import javax.sql.DataSource
import org.kisobran.top.model.Entry
import org.kisobran.top.repository.WinnersRepository
import shapeless._
import slick.jdbc.JdbcProfile
import slick.lifted
import slickless._

import scala.concurrent.{ExecutionContext, Future}

class SlickWinnersRepository(dataSource: DataSource)(
  implicit val profile: JdbcProfile,
  implicit val executionContext: ExecutionContext
) extends WinnersRepository with BaseRepo {

  import profile.api._

  val db: profile.backend.DatabaseDef = profile.backend.Database.forDataSource(dataSource, Some(1))

  class WinnersTable(tag: Tag) extends Table[Winner](tag, "WINNERS") {
    def id = column[String]("ID", O.PrimaryKey) // This is the primary key column

    def song1 = column[String]("SONG_1")

    def song2 = column[String]("SONG_2")

    def song3 = column[String]("SONG_3")

    def song4 = column[String]("SONG_4")

    def song5 = column[String]("SONG_5")

    def song6 = column[String]("SONG_6")

    def song7 = column[String]("SONG_7")

    def song8 = column[String]("SONG_8")

    def song9 = column[String]("SONG_9")

    def song10 = column[String]("SONG_10")

    def artist1 = column[String]("ARTIST_1")

    def artist2 = column[String]("ARTIST_2")

    def artist3 = column[String]("ARTIST_3")

    def artist4 = column[String]("ARTIST_4")

    def artist5 = column[String]("ARTIST_5")

    def artist6 = column[String]("ARTIST_6")

    def artist7 = column[String]("ARTIST_7")

    def artist8 = column[String]("ARTIST_8")

    def artist9 = column[String]("ARTIST_9")

    def artist10 = column[String]("ARTIST_10")

    def year = column[Int]("YEAR")

    def desc = column[String]("DESC")

    def ytLink = column[Option[String]]("YT_LINK")

    def updatedAt = column[Long]("UPDATED_AT")

    def * = (
      id :: desc ::
        song1 :: song2 :: song3 :: song4 :: song5 :: song6 :: song7 :: song8 :: song9 :: song10 ::
        artist1 :: artist2 :: artist3 :: artist4 :: artist5 :: artist6 :: artist7 :: artist8 :: artist9 :: artist10 ::
        year :: updatedAt :: ytLink :: HNil
      ).mappedWith(Generic[Winner])
  }

  val winners = lifted.TableQuery[WinnersTable]

  def idGenerator = UUID.randomUUID().toString

  override def createWinner(entries: Seq[Entry], description: String, year: Int, ytLink: Option[String] = None): Future[Option[Winner]] = {
    val row = Winner(
      id = idGenerator,
      description = description,
      song1 = entries.head.song,
      song2 = entries(1).song,
      song3 = entries(2).song,
      song4 = entries(3).song,
      song5 = entries(4).song,
      song6 = entries(5).song,
      song7 = entries(6).song,
      song8 = entries(7).song,
      song9 = entries(8).song,
      song10 = entries(9).song,
      artist1 = entries.head.artist,
      artist2 = entries(1).artist,
      artist3 = entries(2).artist,
      artist4 = entries(3).artist,
      artist5 = entries(4).artist,
      artist6 = entries(5).artist,
      artist7 = entries(6).artist,
      artist8 = entries(7).artist,
      artist9 = entries(8).artist,
      artist10 = entries(9).artist,
      year,
      createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
      ytLink = ytLink
    )

    db.run(winners += row).map {
      case 1 => Some(row)
      case _ => None
    }
  }

  def winners(year: Option[Int]): Future[Seq[Winner]] = {
    year match {
      case Some(y) => db.run(winners.filter(_.year === y).take(1).result.head).map(x => Seq(x))
      case None => db.run(winners.take(100).result)
    }
  }

  override def tables: Seq[SomeTable] = Seq(winners.asInstanceOf[SomeTable])
}

final case class Winner(id: String,
                        description: String,
                        song1: String,
                        song2: String,
                        song3: String,
                        song4: String,
                        song5: String,
                        song6: String,
                        song7: String,
                        song8: String,
                        song9: String,
                        song10: String,
                        artist1: String,
                        artist2: String,
                        artist3: String,
                        artist4: String,
                        artist5: String,
                        artist6: String,
                        artist7: String,
                        artist8: String,
                        artist9: String,
                        artist10: String,
                        year: Int,
                        createdAt: Long,
                        ytLink: Option[String]
                       )

object Winner {
  def idGenerator = UUID.randomUUID().toString

  def apply(entries: Seq[Entry], description: String, year: Int, ytLink: Option[String] = None): Winner = {
    Winner(
      id = idGenerator,
      description = description,
      song1 = entries.head.song,
      song2 = entries(1).song,
      song3 = entries(2).song,
      song4 = entries(3).song,
      song5 = entries(4).song,
      song6 = entries(5).song,
      song7 = entries(6).song,
      song8 = entries(7).song,
      song9 = entries(8).song,
      song10 = entries(9).song,
      artist1 = entries.head.artist,
      artist2 = entries(1).artist,
      artist3 = entries(2).artist,
      artist4 = entries(3).artist,
      artist5 = entries(4).artist,
      artist6 = entries(5).artist,
      artist7 = entries(6).artist,
      artist8 = entries(7).artist,
      artist9 = entries(8).artist,
      artist10 = entries(9).artist,
      year,
      createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
      ytLink = ytLink
    )
  }
}