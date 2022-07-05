package market.main.sale

import market.main.receipt.Receipt
import slick.jdbc.SQLiteProfile.api._
import market.main.product

case class Sale(uuid: String,
                receiptUUID: String,
                productUUID: String,
                sellingPrice: Double)

object Sale {

  val receipts: TableQuery[Sales] = TableQuery[Sales]

  def tupled = (Sale.apply _).tupled

  class Sales(tag: Tag) extends Table[Sale](tag, "Sale") {
    def uuid = column[String]("upc")

    def receiptUUID = column[String]("check_number")

    def productUUID = column[String]("product_number")

    def sellingPrice = column[Double]("selling_price")

    def receiptUUID_fk =
      foreignKey("pass_fk", receiptUUID,
        Receipt.receipts)(_.uuid,
        onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.NoAction)

    def productUUID_fk =
      foreignKey("room_fk", productUUID,
        product.Product.products)(_.uuid,
        onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Cascade)


    override def * = (uuid, receiptUUID, productUUID, sellingPrice) <> (Sale.tupled, Sale.unapply)
  }
}
