package org.kisobran.top.repository

import org.kisobran.top.db.{Winner, EmbeddedUtil}
import org.kisobran.top.model.Entry

import scala.concurrent.Future
import scala.util.Try

trait WinnersRepository {

  def winners(year: Option[Int]): Future[Seq[Winner]]

  def createWinner(entries: Seq[Entry], description: String, year: Int, ytLink: Option[String] = None): Future[Option[Winner]]

}

object InMemoryWinnersRepository extends WinnersRepository {
  import EmbeddedUtil._

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

  val Entries2013 = Seq(
    Entry("Arctic Monkeys", "Do I Wanna Know", 1, 1),
    Entry("Arcade Fire", "Reflektor", 2, 2),
    Entry("!!!", "One Girl / One Boy", 3, 4),
    Entry("Arcade Fire", "Afterlife ", 4, 5),
    Entry("Daft Punk", "Get Lucky", 5, 7),
    Entry("James Blake", "Retrograde", 6, 6),
    Entry("Arctic Monkeys", "Why’d You Only Call Me When You’re High?", 7, 7),
    Entry("The National", "Sea of love", 8, 8),
    Entry("Rudimental", "Waiting All Night", 9, 9),
    Entry("Ti", "Da ti želim dobra jutra", 10, 10)
  )

  val Entries2014 = Seq(
    Entry("Tv On The Radio", "Happy Idiot", 10, 10),
    Entry("Caribou", "Can’t Do Without You", 9, 9),
    Entry("The Black Keys", "Fever", 8, 8),
    Entry("Chet Faker", "Gold", 7, 7),
    Entry("Fka Twigs", "Two Weeks", 6, 6),
    Entry("Klaxons", "There Is No Other Time", 5, 5),
    Entry("The War On Drugs", "Red Eyes", 4, 4),
    Entry("Vance Joy", "Riptide", 3, 3),
    Entry("Alt-j", "Every Other Freckle", 2, 2),
    Entry("Future Islands", "Seasons (Waiting on you)", 1, 1)
  ).reverse

  val Entries2015 = Seq(
    Entry("Years & Years", "King", 1, 1),
    Entry("Tame Impala", "Let It Happen", 2, 2),
    Entry("Coldplay", "Adventure Of A Lifetime", 3, 3),
    Entry("Rattata", "Cream On Chrome", 4, 4),
    Entry("Tame Impala", "The Less I Know The Better", 5, 5),
    Entry("The Weeknd", "Can’t Ceel My Face", 6, 6),
    Entry("Florence + The Machine", "What Kind Of Man", 7, 7),
    Entry("The Chemical Brothers", "Go", 8, 8),
    Entry("Foals", "Mountain At My Gates", 9, 9),
    Entry("Florence + The Machine", "Queen of Peace", 10, 10)
  )

  val Entries2016 = Seq(
    Entry("The XX", "On Hold", 1, 1),
    Entry("Glass Animals", "Life Itself", 2, 2),
    Entry("Childish Gambino", "Redbone", 3, 3),
    Entry("White lies", "Take It Out On Me", 4, 4),
    Entry("The Weeknd ft Daft Punk", "Starboy", 5, 5),
    Entry("Rihanna", "Kiss It Better", 6, 6),
    Entry("Flume", "Never Be Like You", 7, 7),
    Entry("Beyonce", "Formation", 8, 8),
    Entry("The Last Shadow Puppets", "Miracle Aligner", 9, 9),
    Entry("Banks", "Gemini Feed", 10, 10)
  )

  val Entries2017 = Seq(
    Entry("Arcade Fire", "Everything Now", 1, 1),
    Entry("Portugal The Man", "Feel It Still", 2, 2),
    Entry("Banks", "Underdog", 3, 3),
    Entry("Bonobo", "No Reason", 4, 4),
    Entry("!!!", "The One 2", 5, 5),
    Entry("The National", "Guilty Party", 6, 6),
    Entry("Arcade Fire", "Put Your Money On Me", 7, 7),
    Entry("Tove Lo", "Disco Tits", 8, 8),
    Entry("Lorde", "Green Light", 9, 9),
    Entry("Kasabian", "You're In Love With A Psycho", 10, 10)
  )

  val Entries2018 = Seq(
    Entry("Vojko V", "Ne Može", 1, 1),
    Entry("Childish Gambino", "This Is America", 2, 2),
    Entry("Lykke Li", "Deep End", 3, 3),
    Entry("Bicep", "Opal - Four Tet Remix", 4, 4),
    Entry("Arctic Monkeys", "Four Out Of Five", 5, 5),
    Entry("Mark Ronson", "Nothing Breaks Like a Heart (feat. Miley Cyrus)", 6, 6),
    Entry("Coby", "Biseri iz Blata", 7, 7),
    Entry("Billie Eilish", "When The Party's Over", 8, 8),
    Entry("Drake", "Nice For What", 9, 9),
    Entry("Lana Del Rey", "Mariners Apartment Complex", 10, 10)
  )

  val Entries2019 = Seq(
    Entry("Local Natives", "When Am I Gonna Loose You", 1, 1),
    Entry("Tame Impala", "Borderline", 2, 2),
    Entry("Tyle, The Creator", "EARFQUAKE", 3, 3),
    Entry("White Lies", "Tokyo", 4, 4),
    Entry("Foal", "In Degrees", 5, 5),
    Entry("Mark Ronson", "Late Night Feelings (feat. Lykke Li)", 6, 6),
    Entry("Buč Kesidi", "Đuskanje ne pomaže", 7, 7),
    Entry("Altin Gün", "Süpürgesi Yoncadan", 8, 8),
    Entry("James Blake", "Barefoot In The Park (feat ROSALÍA", 9, 9),
    Entry("Billie Eilish", "bad guy", 10, 10)
  )


  val Entries2020 = Seq(
    Entry("Caribou", "Never Come Back", 1, 1),
    Entry("z++", "Ljeto već je Gotovo", 2, 2),
    Entry("Tom Misch", "Nightrider", 3, 3),
    Entry("Matt Berninger", "One More Second", 4, 4),
    Entry("Washed", "Out Too Late", 5, 5),
    Entry("Four Tet", "Baby", 6, 6),
    Entry("Sufjan Stevens", "Video Game", 7, 7),
    Entry("Altin Gün", "Süpürgesi Yoncadan", 8, 8),
    Entry("Fontaines D.C.", "I Don't Belong", 9, 9),
    Entry("Disclosure", "Tondo", 10, 10)
  )

  val Entries2021 = Seq(
    Entry("Zicer Inc.", "Žad", 1, 1),
    Entry("Yves Tumor", "Jackie", 2, 2),
    Entry("Billie Eilish", "Happier Than Ever", 3, 3),
    Entry("Tyler, The Creator", "SWEET / I THOUGHT YOU WANTED TO DANCE", 4, 4),
    Entry("Lil Nas X", "INDUSTRY BABY", 5, 5),
    Entry("Jessie Ware", "Hot N Heavy", 6, 6),
    Entry("Sitzpinker", "Sudan", 7, 7),
    Entry("Fred again..", " Marea (We’ve Lost Dancing)", 8, 8),
    Entry("PinkPantheress", "Pain", 9, 9),
    Entry("Xanadu", "Kopenhagen", 10, 10)
  )

  val Winners: Map[Int, Winner] = Map(
    2011 -> Winner(
      Entries2011,
      "Pa M83 je napravio himnu Kišobrana, djuskanje uz Foster The People i ljubav za I Follow Rivers dok nam se nije smučila",
      2011,
      Some("https://youtu.be/dX3k_QDnzHE").map {
        toEmbedded
      }
    ),
    2012 -> Winner(
      Entries2012,
      "R U Mine? je hitčina od prvog slušanja, Frank Ocean odličan debi i The XX se vratili posle previše godina pauze",
      2012,
      Some("https://youtu.be/VQH8ZTgna3Q").map {
        toEmbedded
      }
    ),
    2013 -> Winner(
      Entries2013,
      "Drugu godinu za redom Arctic Monkeys na prvom mestu, dokle više Get Lucky, opasni Arcade Fire i The National i prelepi domaći debi Ti",
      2013,
      Some("https://youtu.be/F586JktJyEg").map {
        toEmbedded
      }
    ),
    2014 ->
      Winner(
        Entries2014,
        "Future Islands je bomba, Klaxons je žurka, FKA Twigs je devojka koju gledaš krišom a Caribou je ljubav.",
        2014,
        Some("https://youtu.be/-5Ae-LhMIG0").map {
          toEmbedded
        }
      ),
    2015 ->
      Winner(
        Entries2015,
        "Na Kišobran žurkama je King izabran za pesmu godine, Let it happen hoćemo live a Florence + The Machine ima album koji će se pamtiti",
        2015,
        Some("https://youtu.be/g_uoH6hJilc").map {
          toEmbedded
        }
      ),
    2016 ->
      Winner(
        Entries2016,
        "Koliko mi volimo The XX, Glass Animals je letnja indie albumčina, Redbone na repeat a Beyonce na tron",
        2016,
        Some("https://youtu.be/blJKoXWlqJk").map {
          toEmbedded
        }
      ),
    2017 ->
      Winner(
        Entries2017,
        "Arcade Fire je veliki i kada je slab, Portugal The Man smo puštali na vrhuncu žurke, The National opet pun pogodak, !!! razvalili Dom Omladine",
        2017,
        Some("https://youtu.be/zC30BYR3CUk").map {
          toEmbedded
        }
      ),
    2018 ->
      Winner(
        Entries2018,
        "Vojko V je glas Balkana, Arctic Monkeys su peto mesto od deset a Coby ima najgledaniji spot od većine pesama u listi",
        2018,
        Some("https://youtu.be/fOEp1GiVsWs").map {
          toEmbedded
        }
      ),
    2019 ->
      Winner(
        Entries2019,
        "Local Natives bolji nego ikad, Buč Kesidi brani domaće boje a lista ne može bez Foals, White Lies i Tame Impala-e",
        2019,
        Some("https://youtu.be/oWwytT5JAdM").map {
          toEmbedded
        }
      ),
    2020 ->
      Winner(
        Entries2020,
        "Jedna - nikad se ne ponovila godina - koja će ostati upamćena po svemu samo ne po muzici. Ako vam treba neka uteha i sigurnost samo da znate Caribou je i dalje najbolji",
        2020,
        Some("https://youtu.be/I3hDvOL7E7Y").map {
          toEmbedded
        }
      ),
    2021 ->
      Winner(
        Entries2021,
        "U godini u kojoj samo muzika nije zakazala Žađ je zasluženo izdominirao",
        2020,
        Some("https://youtu.be/mUP473divsc").map {
          toEmbedded
        }
      )
  )

  override def winners(year: Option[Int]): Future[Seq[Winner]] = {
    year match {
      case Some(y) => Future.successful(Try(Seq(Winners(y))).getOrElse(Seq.empty))
      case None => Future.successful(Winners.values.toSeq.sortBy(_.year).reverse)
    }
  }

  override def createWinner(entries: Seq[Entry], description: String, year: Int, ytLink: Option[String]): Future[Option[Winner]] =
    Future.successful(None)
}
