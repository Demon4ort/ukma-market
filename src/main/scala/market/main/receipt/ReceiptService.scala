package market.main.receipt

import cats.implicits.catsSyntaxOptionId
import market.main.receipt.ReceiptRepository.Query
import market.utils.Repository.RepositoryOps
import slick.jdbc.SQLiteProfile

class ReceiptService(implicit val db: SQLiteProfile.backend.Database) {
  private val repository = new ReceiptRepository

  def all = repository.findBy(Query()).future


  def delete(uuid: String) = repository.delete(Query(uuid.some)).future
}
