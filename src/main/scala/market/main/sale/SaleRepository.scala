package market.main.sale

import market.App
import market.main.sale.Sale.Sales
import market.main.sale.SaleRepository.Query
import market.utils.Repository
import slick.jdbc.SQLiteProfile
import slick.jdbc.SQLiteProfile.api._

class SaleRepository extends Repository[Sale, Sales, Query] {
  override implicit val db: SQLiteProfile.backend.Database = App.db

  override def tableQuery = TableQuery[Sales]

  override def findByQuery(query: Query): SQLiteProfile.api.Query[Sales, Sale, Seq] = tableQuery.filterOpt(query.uuid)((t, q) => t.uuid === q)
}

object SaleRepository {
  case class Query(uuid: Option[String] = None)
}
