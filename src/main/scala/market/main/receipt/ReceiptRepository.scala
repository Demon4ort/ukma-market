package market.main.receipt

import market.App
import market.main.receipt.Receipt.Receipts
import market.main.receipt.ReceiptRepository.Query
import market.utils.Repository
import slick.jdbc.SQLiteProfile
import slick.jdbc.SQLiteProfile.api._
import slick.lifted.TableQuery

class ReceiptRepository extends Repository[Receipt, Receipts, Query] {
  override implicit val db: SQLiteProfile.backend.Database = App.db

  override def tableQuery = TableQuery[Receipts]

  override def findByQuery(query: Query): SQLiteProfile.api.Query[Receipts, Receipt, Seq] = tableQuery.filter(_.uuid === query.uuid)
}

object ReceiptRepository {
  case class Query(uuid: String)
}
