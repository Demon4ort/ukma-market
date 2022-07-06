package market.main.store_product

import cats.implicits.catsSyntaxOptionId
import market.main.store_product.StoreProductRepository.Query
import market.utils.Repository.RepositoryOps
import slick.jdbc.SQLiteProfile
import scala.concurrent.ExecutionContext.Implicits.global

class StoreProductService(implicit val db: SQLiteProfile.backend.Database) {
  private val repository = new StoreProductRepository()

  def all = repository.findBy(Query()).future


  def delete(uuid: String) = repository.delete(Query(uuid.some)).future

  def upsert(entity: StoreProduct) = {
    val query = Query(uuid = entity.uuid.some)
    for {
      entityOpt <- repository.findOptionBy(query).future
      entity <- entityOpt match {
        case Some(_) => repository.update(query, _ => entity).future
        case None => repository.upsert(entity).future
      }
    } yield entity
  }
}