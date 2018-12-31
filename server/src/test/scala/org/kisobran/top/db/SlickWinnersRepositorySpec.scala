package org.kisobran.top.db

import org.kisobran.top.model.Entry
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec, Matchers}
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import slick.jdbc.H2Profile

import scala.concurrent.ExecutionContext

class SlickWinnersRepositorySpec extends FlatSpec
  with Matchers
  with BeforeAndAfterEach
  with BeforeAndAfterAll
  with ScalaFutures
  with IntegrationPatience {

  val slickWinnerRepository = new SlickWinnersRepository(DbTestConfiguration.testMySQL)(H2Profile, ExecutionContext.global)

  override def beforeEach(): Unit = {
    slickWinnerRepository.ensureTablesPresent(true)
  }

  "SlickWinner repository" should "play nice" in {
    slickWinnerRepository.createWinner(
      (1 to 10).map { i => Entry(s"artist${i}", s"song${i}", i, i) },
      "use@somebody.com",
      2018
    ).futureValue

    slickWinnerRepository.createWinner(
      (1 to 10).map { i => Entry(s"artist${i}", s"song${i}", i, i) },
      "use@somebody.com",
      2017
    ).futureValue

    val returned = slickWinnerRepository.winners(Some(2017)).futureValue.head

    returned.song1 should be("song1")
    returned.song2 should be("song2")
    returned.song3 should be("song3")
    returned.song4 should be("song4")
    returned.song5 should be("song5")
    returned.song6 should be("song6")
    returned.song7 should be("song7")
    returned.song8 should be("song8")
    returned.song9 should be("song9")
    returned.song10 should be("song10")

    returned.artist1 should be("artist1")
    returned.artist2 should be("artist2")
    returned.artist3 should be("artist3")
    returned.artist4 should be("artist4")
    returned.artist5 should be("artist5")
    returned.artist6 should be("artist6")
    returned.artist7 should be("artist7")
    returned.artist8 should be("artist8")
    returned.artist9 should be("artist9")
    returned.artist10 should be("artist10")
    returned.year should be(2017)

    slickWinnerRepository.winners(None).futureValue.size should be (2)

  }
}
