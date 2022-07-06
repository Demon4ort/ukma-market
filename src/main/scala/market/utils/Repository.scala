package market.utils

import cats.instances.string
import com.typesafe.config.ConfigFactory
import market.utils.Errors.DatabaseError
import slick.dbio.Effect
import slick.jdbc
import slick.jdbc.SQLiteProfile
import slick.jdbc.SQLiteProfile.api._
import slick.sql.{FixedSqlAction, FixedSqlStreamingAction, SqlAction}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.reflect.ClassTag


//ER-модель складається з 6 сутностей: Працівник, Чек, Карта клієнта, Товар у магазині, Товар, Категорія.
abstract class Repository[
  E <: Entity,
  EntityTable <: Table[E],
  EntityQuery](implicit tag: ClassTag[E]) {


  //  implicit val db: jdbc.SQLiteProfile.backend.Database = Database.forConfig("db", ConfigFactory.load("application.conf"))
  implicit val db: jdbc.SQLiteProfile.backend.Database

  def findByQuery(query: EntityQuery): Query[EntityTable, E, Seq]

  def tableQuery: TableQuery[EntityTable]


  def init = tableQuery.schema.createIfNotExists

  def findOneBy(query: EntityQuery): DBIOAction[E, NoStream, Effect.Read with Effect] = {
    findByQuery(query).take(1).result.headOption.flatMap {
      case Some(entity) => DBIO.successful(entity)
      case None => DBIO.failed(DatabaseError("Entity not Found"))
    }
  }

  def findOptionBy(query: EntityQuery): SqlAction[Option[E], NoStream, Effect.Read] =
    findByQuery(query).take(1).result.headOption


  def findBy(query: EntityQuery): FixedSqlStreamingAction[Seq[E], E, Effect.Read] =
    findByQuery(query).result


  def countAll = tableQuery.size.result

  def countBy(query: EntityQuery): FixedSqlAction[Int, _root_.slick.jdbc.SQLiteProfile.api.NoStream, Effect.Read] =
    findByQuery(query).size.result

  def insert(entities: Seq[E]): DBIOAction[Seq[E], NoStream, Effect.Write] = {
    tableQuery.forceInsertAll(entities).map(_ => entities)
  }


  def upsert(entity: E): DBIOAction[E, NoStream, Effect.Write] =
    tableQuery.insertOrUpdate(entity).map(_ => entity)


  def create(entities: Seq[E]): FixedSqlAction[Option[Int], NoStream, Effect.Write] =
    tableQuery ++= entities


  def update(query: EntityQuery, update: E => E): DBIOAction[E, NoStream, Effect.Read with Effect.Write with Effect.Transactional] =
    (for {
      found <- findOneBy(query)
      res = update(found)
      _ <- tableQuery.insertOrUpdate(res)
    } yield res).transactionally

  def delete(query: EntityQuery): FixedSqlAction[Int, NoStream, Effect.Write] =
    findByQuery(query).delete

  def deleteAll(): FixedSqlAction[Int, NoStream, Effect.Write] =
    tableQuery.delete

}

object Repository {

  def enumMapper[E <: Enumeration](`enum`: E): BaseColumnType[E#Value] = {
    MappedColumnType.base[E#Value, String](_.toString, string => `enum`.withName(string))
  }

  final implicit class RepositoryOps[R](val actions: DBIOAction[R, NoStream, Nothing]) extends AnyVal {

    def future(implicit db: SQLiteProfile.backend.Database): Future[R] = db.run(actions)

  }

}
