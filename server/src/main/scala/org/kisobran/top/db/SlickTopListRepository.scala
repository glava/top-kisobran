package org.kisobran.top.db

import java.time.{LocalDateTime, ZoneOffset}
import java.util.UUID

import javax.sql.DataSource
import org.kisobran.top.model.Entry
import org.kisobran.top.repository.TopListRepository
import shapeless._
import slick.jdbc.JdbcProfile
import slick.lifted
import slickless._

import scala.concurrent.{ExecutionContext, Future}

class SlickTopListRepository(dataSource: DataSource)(implicit val profile: JdbcProfile, implicit val executionContext: ExecutionContext) extends TopListRepository with BaseRepo{

  import profile.api._

  val db: profile.backend.DatabaseDef = profile.backend.Database.forDataSource(dataSource, Some(1))

  class TopListTable(tag: Tag) extends Table[TopListEntries](tag, "TOP_LIST") {
    def id = column[String]("ID", O.PrimaryKey) // This is the primary key column

    def title = column[String]("TITLE")

    def userEmail = column[Option[String]]("USER_EMAIL")

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

    def enabled = column[Boolean]("ENABLED")

    def ytLink = column[Option[String]]("YT_LINK")

    def updatedAt = column[Long]("UPDATED_AT")

    def * = (
      id :: title :: userEmail ::
        song1 :: song2 :: song3 :: song4 :: song5 :: song6 :: song7 :: song8 :: song9 :: song10 ::
        artist1 :: artist2 :: artist3 :: artist4 :: artist5 :: artist6 :: artist7 :: artist8 :: artist9 :: artist10 :: year :: updatedAt :: enabled :: ytLink :: HNil
      ).mappedWith(Generic[TopListEntries])
  }

  val topList = lifted.TableQuery[TopListTable]

  def idGenerator = UUID.randomUUID().toString

  override def createTopList(userEmail: Option[String], entries: Seq[Entry], listName: String, enabled: Boolean = false): Future[Option[TopListEntries]] = {
    val row = TopListEntries(
      idGenerator,
      listName,
      userEmail,
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
      year = 2018,
      createdAt = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
      enabled = enabled,
      ytLink = None
    )

    db.run(topList += row).map {
      case 1 => Some(row)
      case _ => None
    }
  }

  override def findTopList(id: String): Future[Option[TopListEntries]] =
    db.run(topList.filter(_.id === id).take(1).result.headOption)

  override def select(limit: Int, offset: Int): Future[Seq[TopListEntries]] =
    db.run(topList.filter(_.enabled === true)
      .sortBy(_.updatedAt.desc)
      .drop(offset)
      .take(limit)
      .result
    )

  override def count() =
    db.run(topList.size.result)

  override def tables: Seq[SomeTable] = Seq(topList.asInstanceOf[SomeTable])

  override def update(id: String, ytLink: Option[String]): Future[Int] = {
    val en = for { list <- topList.filter(_.id === id) } yield (list.enabled, list.updatedAt, list.ytLink)
    db.run(en.update(true, LocalDateTime.now().toEpochSecond(ZoneOffset.UTC), ytLink))
  }
}

final case class TopListEntries(id: String,
                                title: String,
                                userEmail: Option[String],
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
                                enabled: Boolean,
                                ytLink: Option[String]
                               )