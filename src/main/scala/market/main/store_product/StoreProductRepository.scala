package market.main.store_product

import market.App
import market.main.store_product.StoreProduct.StoreProducts
import market.main.store_product.StoreProductRepository.Query
import market.utils.Repository
import slick.jdbc.SQLiteProfile
import slick.lifted.TableQuery
import slick.jdbc.SQLiteProfile.api._

class StoreProductRepository extends Repository[StoreProduct, StoreProducts, Query] {
  override implicit val db: SQLiteProfile.backend.Database = App.db

  override def findByQuery(query: Query): SQLiteProfile.api.Query[StoreProducts, StoreProduct, Seq] =
    tableQuery.filter(_.uuid === query.uuid)

  override def tableQuery = TableQuery[StoreProducts]
}

object StoreProductRepository {
  case class Query(uuid: String)
}
