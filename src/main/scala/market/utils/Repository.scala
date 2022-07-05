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
  Entity: ClassTag,
  EntityTable <: Table[Entity],
  EntityQuery] {


  //  implicit val db: jdbc.SQLiteProfile.backend.Database = Database.forConfig("db", ConfigFactory.load("application.conf"))
  implicit val db: jdbc.SQLiteProfile.backend.Database

  def findByQuery(query: EntityQuery): Query[EntityTable, Entity, Seq]

  def tableQuery: TableQuery[EntityTable]


  def init = tableQuery.schema.createIfNotExists

  def findOneBy(query: EntityQuery): DBIOAction[Entity, NoStream, Effect.Read with Effect] = {
    findByQuery(query).take(1).result.headOption.flatMap {
      case Some(entity) => DBIO.successful(entity)
      case None => DBIO.failed(DatabaseError("Entity not Found"))
    }
  }

  def findOptionBy(query: EntityQuery): SqlAction[Option[Entity], NoStream, Effect.Read] =
    findByQuery(query).take(1).result.headOption


  def findBy(query: EntityQuery): FixedSqlStreamingAction[Seq[Entity], Entity, Effect.Read] =
    findByQuery(query).result


  def countAll = tableQuery.size.result

  def countBy(query: EntityQuery): FixedSqlAction[Int, _root_.slick.jdbc.SQLiteProfile.api.NoStream, Effect.Read] =
    findByQuery(query).size.result

  def insert(entities: Seq[Entity]): DBIOAction[Seq[Entity], NoStream, Effect.Write] = {
    tableQuery.forceInsertAll(entities).map(_ => entities)
  }


  def upsert(entity: Entity): DBIOAction[Entity, NoStream, Effect.Write] =
    tableQuery.insertOrUpdate(entity).map(_ => entity)

  def create(entities: Seq[Entity]): FixedSqlAction[Seq[Entity], NoStream, Effect.Write] =
    tableQuery.returning(tableQuery) ++= entities


  def create(entity: Entity): DBIOAction[Entity, NoStream, Effect.Write] =
    (tableQuery += entity).map(_ => entity)


  def update(query: EntityQuery, update: Entity => Entity): DBIOAction[Entity, NoStream, Effect.Read with Effect with Effect.Write with Effect.Transactional] =
    (for {
      found <- findOneBy(query)
      updated <- upsert(update(found))
    } yield updated).transactionally

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
