package market.main.sale

import market.main.receipt.Receipt
import slick.jdbc.SQLiteProfile.api._
import market.main.product
import market.main.store_product.StoreProduct
import market.utils.Entity
import scalafx.beans.property.ObjectProperty

import java.util.UUID

case class Sale(uuid: String,
                receiptUUID: String,
                productNumber: Int,
                sellingPrice: Double) extends Entity {
  val _sellingPrice = new ObjectProperty(this, "selling price", sellingPrice)
  val _productNumber = new ObjectProperty(this, "number", productNumber)
}

object Sale {

  val receipts: TableQuery[Sales] = TableQuery[Sales]

  def tupled = (Sale.apply _).tupled

  class Sales(tag: Tag) extends Table[Sale](tag, "Sale") {
    def uuid = column[String]("upc")

    def receiptUUID = column[String]("check_number")

    def productNumber = column[Int]("product_number")

    def sellingPrice = column[Double]("selling_price")

    def receiptUUID_fk =
      foreignKey("pass_fk", receiptUUID,
        Receipt.receipts)(_.uuid,
        onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.NoAction)

    def productUUID_fk =
      foreignKey("room_fk", uuid,
        StoreProduct.storeProducts)(_.uuid,
        onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)


    override def * = (uuid, receiptUUID, productNumber, sellingPrice) <> (Sale.tupled, Sale.unapply)
  }
}
