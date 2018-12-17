package org.kisobran.top

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.kisobran.top.model.Entry
import org.kisobran.top.repository.TopListRepository

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

object Crawler {
  implicit val ex = ExecutionContext.global

  def splitNum(n: Int, a: Array[String]): Array[String] = {
    if (n == 11) a
    else {
      splitNum(n + 1, a.flatMap(_.split(s"${n}\\."))).map(_.trim()).filter(_ != "")
    }
  }

  def splitArtist(s: String) = s.split("\u2013").map(_.trim())

  def extractEntries(post: Element): Seq[Entry] = {
    splitNum(1, Array(post.select(".entry-content").text()))
      .map(splitArtist).zipWithIndex.map(t => Entry(t._1.toSeq.head, t._1(1), t._2, t._2))
  }


  val all = Map(2017 -> Seq(
    "http://topdeset.kisobran.org/category/2017/",
    "http://topdeset.kisobran.org/category/2017/page/2/",
    "http://topdeset.kisobran.org/category/2017/page/3/"
  ),
    2016 -> Seq(
      "http://topdeset.kisobran.org/category/2016/",
      "http://topdeset.kisobran.org/category/2016/page/2/",
      "http://topdeset.kisobran.org/category/2016/page/3/",
    ),
    2015 -> Seq(
      "http://topdeset.kisobran.org/category/2015/",
      "http://topdeset.kisobran.org/category/2015/page/2/",
      "http://topdeset.kisobran.org/category/2015/page/3/",
      "http://topdeset.kisobran.org/category/2015/page/4/"
    ),
    2014 -> Seq(
      "http://topdeset.kisobran.org/category/2014/",
      "http://topdeset.kisobran.org/category/2014/page/2/",
      "http://topdeset.kisobran.org/category/2014/page/3/",
      "http://topdeset.kisobran.org/category/2014/page/4/"
    ),
    2013 -> Seq(
      "http://topdeset.kisobran.org/category/2013/",
      "http://topdeset.kisobran.org/category/2013/page/2/",
      "http://topdeset.kisobran.org/category/2013/page/3/",
      "http://topdeset.kisobran.org/category/2013/page/4/",
      "http://topdeset.kisobran.org/category/2013/page/5/",
      "http://topdeset.kisobran.org/category/2013/page/6/"
    )

  )

  def extract(year: Int, isTail: Boolean, skipYt: Boolean, p: Int)(repo: TopListRepository) = {
    val page = all(year)(p)

    val doc = Jsoup.connect(page).get()
    val posts = if (isTail) doc.select(".post").asScala.tail else doc.select(".post").asScala
    Future.sequence {
      posts.map { post =>
        val yt: Option[String] = Try(post.select("iframe").attr("src")).toOption.flatMap(ytLink =>
          if (ytLink.isEmpty) None else Some(ytLink)
        )
        val title = post.select(".entry-title").text
        println(title)
        println(yt)
        val entries: Seq[Entry] = extractEntries(post)
        repo.createTopList(None, entries, title, true, year, yt)
      }

    }
  }

}
