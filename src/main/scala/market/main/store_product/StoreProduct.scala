package market.main.store_product

import slick.jdbc.SQLiteProfile.api._
import market.main.product
import slick.lifted.ProvenShape


case class StoreProduct(uuid: String,
                        promUUID: Option[String],
                        productUUID: String,
                        sellingPrice: Double,
                        productsNumber: Int,
                        promotionalProduct: Boolean)


object StoreProduct {

  val storeProducts: TableQuery[StoreProducts] = TableQuery[StoreProducts]

  def tupled = (StoreProduct.apply _).tupled

  class StoreProducts(tag: Tag) extends Table[StoreProduct](tag, "Store_Product") {

    def uuid = column[String]("upc")

    def promUUID = column[Option[String]]("upc_prom")

    def productUUID = column[String]("product_id")

    def sellingPrice = column[Double]("selling_price")

    def productsNumber = column[Int]("products_number")

    def promotionalProduct = column[Boolean]("promotional_product")

    def promUUID_fk =
      foreignKey("pass_fk", promUUID,
        storeProducts)(_.promUUID,
        onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.SetNull)

    def productUUID_fk =
      foreignKey("room_fk", productUUID,
        product.Product.products)(_.uuid,
        onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.NoAction)

    override def * : ProvenShape[StoreProduct] =
      (uuid, promUUID, productUUID, sellingPrice, productsNumber, promotionalProduct) <> (StoreProduct.tupled, StoreProduct.unapply)
  }
}
