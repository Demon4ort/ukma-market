package market.main.product

import market.App
import market.main.product.Product.Products
import market.main.product.ProductRepository.Query
import market.utils.Repository
import slick.jdbc
import slick.jdbc.SQLiteProfile
import slick.lifted.TableQuery
import slick.jdbc.SQLiteProfile.api._

class ProductRepository extends Repository[Product, Products, Query] {

  override def findByQuery(query: Query): SQLiteProfile.api.Query[Products, Product, Seq] =
    tableQuery.filter(_.uuid === query.uuid)

  override def tableQuery = TableQuery[Products]

  override implicit val db: jdbc.SQLiteProfile.backend.Database = App.db
}

object ProductRepository {

  case class Query(uuid: String)

}
