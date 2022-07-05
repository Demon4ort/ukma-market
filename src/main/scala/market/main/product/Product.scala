package market.main.product

import slick.jdbc.SQLiteProfile.api._


case class Product(uuid: String,
                   categoryUUID: String,
                   name: String,
                   characteristics: String)

object Product {

  val products: TableQuery[Products] = TableQuery[Products]

  def tupled = (Product.apply _).tupled

  class Products(tag: Tag) extends Table[Product](tag, "Product") {

    def uuid = column[String]("product_id")

    def categoryUUID = column[String]("category_id")

    def name = column[String]("product_name")

    def characteristics = column[String]("product_characteristics")

    override def * = (uuid, categoryUUID, name, characteristics) <> (Product.tupled, Product.unapply)
  }
}
