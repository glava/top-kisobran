package org.kisobran.top.repository

import org.kisobran.top.db.{Winner, YtUtil}
import org.kisobran.top.model.Entry

import scala.concurrent.Future
import scala.util.Try

trait WinnersRepository {

  def winners(year: Option[Int]): Future[Seq[Winner]]

  def createWinner(entries: Seq[Entry], description: String, year: Int, ytLink: Option[String] = None): Future[Option[Winner]]

}

object InMemoryWinnersRepository extends WinnersRepository with YtUtil {

  val Entries2011 = Seq(
    Entry("M83", "Midnight City", 1, 1),
    Entry("Veliki Prezir", "To", 2, 2),
    Entry("Foster The People", "Pumped Up Kicks", 3, 4),
    Entry("Lykka Li", "I Follow Rivers ", 4, 5),
    Entry("Nežni Dalibor", "Ne Postojim ", 5, 7),
    Entry("Florence And The Machine", "What The Water Gave Me", 6, 6),
    Entry("The Vaccines ", "If You Wanna", 7, 7),
    Entry("Lana Del Rey", "Blue Jeans ", 8, 8),
    Entry("The Rapture", "How Deep Is Your Love", 9, 9),
    Entry("Tv On The Radio", "Will Do", 10, 10)
  )

  val Entries2012 = Seq(
    Entry("Arctic Monkeys", "R U Mine?", 1, 1),
    Entry("Two Door Cinema Club", "Sun", 2, 2),
    Entry("The XX", "Angels", 3, 4),
    Entry("Stray Dogg", "Disappear ", 4, 5),
    Entry("Beach House", "Myth", 5, 7),
    Entry("Repetitor", "Šteta", 6, 6),
    Entry("M.I.A ", "Bad Girls", 7, 7),
    Entry("Friends", "Blue Jeans ", 8, 8),
    Entry("Frank Ocean", "Pyramids", 9, 9),
    Entry("Die Antwoord", "I Fink U Freaky", 10, 10)
  )

  val Winners: Map[Int, Winner] = Map(
    2011 -> Winner(
      Entries2011,
      "M83 je napravio himnu Kišobrana, skakali smo uz Foster The People i voleli smo I Follow Rivers dok nam se nije smučila",
      2011,
      Some("https://youtu.be/dX3k_QDnzHE").map{ toEmbedded }
    ),
    2012 -> Winner(
      Entries2012,
      "R U Mine? je hitčina od prvog slušanja, Frank Ocean odličan debi i The XX se vratili posle previše godina pauze",
      2012,
      Some("https://youtu.be/VQH8ZTgna3Q").map{ toEmbedded }
    )
  )

  override def winners(year: Option[Int]): Future[Seq[Winner]] = {
    year match {
      case Some(y) => Future.successful(Try(Seq(Winners(y))).getOrElse(Seq.empty))
      case None => Future.successful(Winners.values.toSeq)
    }
  }

  override def createWinner(entries: Seq[Entry], description: String, year: Int, ytLink: Option[String]): Future[Option[Winner]] =
    Future.successful(None)
}
