package market.main.category

import market.utils.Entity
import scalafx.beans.property.StringProperty
import slick.jdbc.SQLiteProfile.api._

import java.util.UUID

case class Category(uuid: String = UUID.randomUUID.toString,
                    name: String) extends Entity {
  //  val _uuid = new StringProperty(this, "category id", uuid)
  val _name = new StringProperty(this, "category name", name)
}

object Category {

  val categories: TableQuery[Categories] = TableQuery[Categories]

  def tupled = (Category.apply _).tupled

  class Categories(tag: Tag) extends Table[Category](tag, "Category") {

    def uuid = column[String]("category_id", O.PrimaryKey)

    def name = column[String]("category_name")

    override def * = (uuid, name) <> (Category.tupled, Category.unapply)
  }
}
