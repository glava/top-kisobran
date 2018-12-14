package org.kisobran.top.db

import slick.jdbc.JdbcProfile
import slick.lifted.{AbstractTable, CanBeQueryCondition, Query, TableQuery}
import slick.relational.RelationalProfile

import scala.concurrent.{ExecutionContext, Future}

trait BaseRepo {
  implicit val profile: JdbcProfile
  implicit val executionContext: ExecutionContext

  type SomeTable = Query[RelationalProfile#Table[_], _, Seq] with TableQuery[RelationalProfile#Table[_]]

  def tables: Seq[SomeTable]

  val db: profile.backend.DatabaseDef

  def ensureTablesPresent(drop: Boolean = false)(): Future[Boolean] = {
    import profile.api._

    lazy val schemas = tables.map(_.schema).reduce((a, b) => a ++ b)

    lazy val dropFuture = if (drop) db.run(schemas.drop) else Future.successful("no drop")

    lazy val createFuture: PartialFunction[Any, Future[Unit]] = {
      case _ => db.run(schemas.create)
    }

    dropFuture
      .flatMap(createFuture)
      .recoverWith(createFuture)
      .map(_ => true)
      .recover {
        case error: Throwable =>
          println(error.getStackTrace.mkString(","))
          false
      }

  }

  implicit class FilterOps[E <: AbstractTable[_], ET, F[_]](query: Query[E, ET, F]) {
    def filterOption[A, T <: slick.lifted.Rep[_]](option: Option[A])(f: A => E => T)(implicit wt: CanBeQueryCondition[T]): Query[E, ET, F] = {
      option.map(a => query.filter(f(a))).getOrElse(query)
    }
  }
}
