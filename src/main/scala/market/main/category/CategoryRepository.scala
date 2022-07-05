package market.main.category

import market.App
import market.main.category.Category.Categories
import market.main.category.CategoryRepository.Query
import market.utils.Repository
import slick.jdbc
import slick.jdbc.SQLiteProfile
import slick.lifted.TableQuery
import slick.jdbc.SQLiteProfile.api._

class CategoryRepository extends Repository[Category, Categories, Query] {


  override def findByQuery(query: Query): SQLiteProfile.api.Query[Categories, Category, Seq] =
    tableQuery.filter(_.uuid === query.uuid)

  override def tableQuery = TableQuery[Categories]

  override implicit val db: jdbc.SQLiteProfile.backend.Database = App.db
}

object CategoryRepository {

  case class Query(uuid: String)

}