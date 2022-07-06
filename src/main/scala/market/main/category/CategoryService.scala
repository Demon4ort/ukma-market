package market.main.category

import cats.implicits.catsSyntaxOptionId
import market.main.category.CategoryRepository.Query
import market.utils.Repository.RepositoryOps
import slick.jdbc.SQLiteProfile

class CategoryService(implicit val db: SQLiteProfile.backend.Database) {

  private val repository = new CategoryRepository


  def add(c: Category) = repository.upsert(c).future

  def delete(uuid: String) = repository.delete(Query(uuid.some)).future


  def all = repository.findBy(Query()).future
}
