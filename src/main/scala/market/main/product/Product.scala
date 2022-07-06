package market.main.product

import market.main.category.Category
import market.utils.Entity
import scalafx.beans.property.{ObjectProperty, StringProperty}
import slick.jdbc.SQLiteProfile.api._


case class Product(uuid: String,
                   categoryUUID: String,
                   name: String,
                   characteristics: String) extends Entity {
  val _uuid = new StringProperty(this, "product id", uuid)
  val _categoryUUID = new StringProperty(this, "category id", categoryUUID)
  val _name = new StringProperty(this, "product name", name)
  val _characteristics = new StringProperty(this, "characteristics", characteristics)
}

object Product {

  val products: TableQuery[Products] = TableQuery[Products]

  def tupled = (Product.apply _).tupled

  class Products(tag: Tag) extends Table[Product](tag, "Product") {

    def uuid = column[String]("product_id")

    def categoryUUID = column[String]("category_id")

    def name = column[String]("product_name")

    def characteristics = column[String]("product_characteristics")

    def categoryUUID_fk = foreignKey("categoryUUID_fk", categoryUUID,
      Category.categories)(_.uuid,
      onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.NoAction)

    override def * = (uuid, categoryUUID, name, characteristics) <> (Product.tupled, Product.unapply)
  }
}
