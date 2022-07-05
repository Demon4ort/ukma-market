package market.main.category

import slick.jdbc.SQLiteProfile.api._

case class Category(uuid: String,
                    name: String)

object Category {

  val categories: TableQuery[Categories] = TableQuery[Categories]

  def tupled = (Category.apply _).tupled

  class Categories(tag: Tag) extends Table[Category](tag, "Category") {

    def uuid = column[String]("category_id", O.PrimaryKey)

    def name = column[String]("category_name")

    override def * = (uuid, name) <> (Category.tupled, Category.unapply)
  }
}
