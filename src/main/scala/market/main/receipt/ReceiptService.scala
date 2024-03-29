package market.main.receipt

import cats.implicits.catsSyntaxOptionId
import market.main.receipt.ReceiptRepository.Query
import market.utils.Repository.RepositoryOps
import slick.jdbc.SQLiteProfile
import scala.concurrent.ExecutionContext.Implicits.global

class ReceiptService(implicit val db: SQLiteProfile.backend.Database) {
  private val repository = new ReceiptRepository

  def all = repository.findBy(Query()).future

  def upsert(entity: Receipt) = {
    val query = Query(uuid = entity.uuid.some)
    for {
      entityOpt <- repository.findOptionBy(query).future
      entity <- entityOpt match {
        case Some(_) => repository.update(query, _ => entity).future
        case None => repository.upsert(entity).future
      }
    } yield entity
  }

  def delete(uuid: String) = repository.delete(Query(uuid.some)).future
}
