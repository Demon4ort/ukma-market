package market.main.customer_card

import cats.implicits.catsSyntaxOptionId
import market.main.customer_card.CustomerCardRepository.Query
import market.utils.Repository.RepositoryOps
import slick.jdbc.SQLiteProfile

class CustomerCardService(implicit val db: SQLiteProfile.backend.Database) {

  private val repository = new CustomerCardRepository


  def add(c: CustomerCard) = repository.upsert(c).future

  def delete(uuid: String) = repository.delete(Query(uuid.some)).future

  def find(uuid: String) = repository.findOneBy(Query(uuid.some)).future


  def all = repository.findBy(Query()).future
}
