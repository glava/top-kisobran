package org.kisobran.top.db

import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.kisobran.top.TopListService
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec, Matchers}
import slick.jdbc.H2Profile

import scala.concurrent.ExecutionContext

class TopListServiceSpec extends FlatSpec
  with Matchers
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with ScalaFutures
  with IntegrationPatience with ScalatestRouteTest {

  val slickTopListRepository = new SlickTopListRepository(DbTestConfiguration.testMySQL)(H2Profile, ExecutionContext.global) {
    override def idGenerator = "1"
  }

  val slickStatsRepository = new SlickStatsRepository(DbTestConfiguration.testMySQL)(H2Profile, ExecutionContext.global)

  override def beforeEach(): Unit = {
    slickTopListRepository.ensureTablesPresent(true)
    slickStatsRepository.ensureTablesPresent(true)
  }

  val topListService = new TopListService(slickTopListRepository, slickStatsRepository)(ExecutionContext.global)

  val routes = topListService.route

  "TopList Service" should "know how to handle creation of list" in {

    val formData = FormData(
      "inputArtist1" -> "Artist1",
      "inputArtist2" -> "Artist2",
      "inputArtist3" -> "Artist3",
      "inputArtist4" -> "Artist4",
      "inputArtist5" -> "Artist5",
      "inputArtist6" -> "Artist6",
      "inputArtist7" -> "Artist7",
      "inputArtist8" -> "Artist8",
      "inputArtist9" -> "Artist9",
      "inputArtist10" -> "Artist10",
      "inputSong1" -> "Song1",
      "inputSong2" -> "Song2",
      "inputSong3" -> "Song3",
      "inputSong4" -> "Song4",
      "inputSong5" -> "Song5",
      "inputSong6" -> "Song6",
      "inputSong7" -> "Song7",
      "inputSong8" -> "Song8",
      "inputSong9" -> "Song9",
      "inputSong10" -> "Song10"
    )

    Post("/vote", formData) ~> routes ~> check {
      val topList: TopListEntries = slickTopListRepository.findTopList("1").futureValue.get

      topList.artist1 should be ("Artist1")
      topList.artist2 should be ("Artist2")
      topList.artist3 should be ("Artist3")
      topList.artist4 should be ("Artist4")
      topList.artist5 should be ("Artist5")
      topList.artist6 should be ("Artist6")
      topList.artist7 should be ("Artist7")
      topList.artist8 should be ("Artist8")
      topList.artist9 should be ("Artist9")
      topList.artist10 should be ("Artist10")

      topList.song1 should be ("Song1")
      topList.song2 should be ("Song2")
      topList.song3 should be ("Song3")
      topList.song4 should be ("Song4")
      topList.song5 should be ("Song5")
      topList.song6 should be ("Song6")
      topList.song7 should be ("Song7")
      topList.song8 should be ("Song8")
      topList.song9 should be ("Song9")
      topList.song10 should be ("Song10")

      val stats = slickStatsRepository.select(10, 0).futureValue

      stats.map(_.artist) should contain allOf (
        "Artist1", "Artist2", "Artist3", "Artist4", "Artist5", "Artist6", "Artist7", "Artist8", "Artist9", "Artist10"
      )

      stats.map(_.song) should contain allOf (
        "Song1", "Song2", "Song3", "Song4", "Song5", "Song6", "Song7", "Song8", "Song9", "Song10"
      )

    }
  }

}
