package market.main.product

import cats.implicits.catsSyntaxOptionId
import market.main.product.ProductRepository.Query
import market.utils.Repository.RepositoryOps
import slick.jdbc.SQLiteProfile
import scala.concurrent.ExecutionContext.Implicits.global

class ProductService(implicit val db: SQLiteProfile.backend.Database) {

  val repository = new ProductRepository

  def all = repository.findBy(Query()).future


  def delete(uuid: String) = repository.delete(Query(uuid.some)).future


  def find(uuid: String) = repository.findOneBy(Query(uuid.some)).future

  def upsert(entity: Product) = {
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
