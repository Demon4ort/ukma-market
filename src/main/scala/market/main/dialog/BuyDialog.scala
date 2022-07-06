package market.main.dialog

import cats.implicits.none
import market.App.stage
import market.main.customer_card.CustomerCardService
import market.main.employee.Employee
import market.main.product.{Product, ProductService}
import market.main.receipt.Receipt
import market.main.sale.Sale
import market.main.store_product.StoreProductService
import scalafx.Includes.jfxDialogPane2sfx
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.layout.{HBox, VBox}

import java.time.LocalDateTime
import java.util.UUID
import scala.concurrent.ExecutionContext
import scala.jdk.CollectionConverters.CollectionHasAsScala

class BuyDialog(employee: Employee)(
  //  implicit val receiptService: ReceiptService,
  //  implicit val saleService: SaleService,
  implicit val customerCardService: CustomerCardService,
  implicit val productService: ProductService,
  implicit val storeProductService: StoreProductService,
  implicit val ec: ExecutionContext) extends Dialog[ReceiptMap] {

  initOwner(stage)
  val receiptUUID: String = UUID.randomUUID.toString
  title = s"Receipt #$receiptUUID"
  width = 700

  val okButtonType = new ButtonType("OK", ButtonData.OKDone)
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)


  val vBox = new VBox() {
    spacing = 20
  }
  val hBox = new HBox() {
    spacing = 20
  }

  case class ToBuy(sale: Sale, product: Product) {
    val _name = new StringProperty(this, "name", product.name)
    val _price = new ObjectProperty(this, "price", sale.sellingPrice)
  }

  val toBuys = ObservableBuffer.empty[ToBuy]


  val products: ObservableBuffer[Product] = ObservableBuffer(productService.all.futureValue: _*)
  val field = new TextField() {
    promptText = "name"
  }


  val tBox = new VBox() {
    spacing = 20
  }
  val tableViewChoose = new TableView[Product](products) {
    columns ++= Seq(
      new TableColumn[Product, String]() {
        text = "product name"
        cellValueFactory = _.value._name
      },
      new TableColumn[Product, String]() {
        text = "characteristics"
        cellValueFactory = _.value._characteristics
      }
    )
  }

  val tableViewBucket = new TableView[ToBuy](toBuys) {
    prefWidth = 200
    prefHeight = 300
    columns ++= Seq(
      new TableColumn[ToBuy, String]() {
        text = "product name"
        cellValueFactory = _.value._name
      },
      new TableColumn[ToBuy, Double]() {
        text = "price"
        cellValueFactory = _.value._price
      },
      new TableColumn[ToBuy, Int]() {
        text = "number"
        cellValueFactory = _.value.sale._productNumber
      }
    )
  }

  tableViewChoose.contextMenu = new ContextMenu(
    new MenuItem("buy") {
      onAction = _ => {
        Option(tableViewChoose.getSelectionModel.getSelectedItem).map { product =>
          val already = tableViewBucket.getItems.asScala.find(_.product.uuid == product.uuid).fold(0)(_.sale.productNumber)
          val store = storeProductService.findByProductUUID(product.uuid).futureValue
          val left = store.productsNumber - already
          val num = new TextInputDialog() {
            contentText = s"Available amount: $left"
            headerText = "Number to buy"
          }.showAndWait().fold(1)(_.toInt)
          if (left >= num) {
            if (left - num == 0) {
              products.removeAll(product)
            }
            val i = toBuys.indexWhere(_.product.uuid == product.uuid)
            if (i != -1) {
              val number = toBuys(i).sale.productNumber
              val sale = new Sale(store.uuid, receiptUUID = receiptUUID, productNumber = num + number, sellingPrice = store.sellingPrice)
              toBuys.update(i, ToBuy(sale, product))
            } else {
              val sale = new Sale(store.uuid, receiptUUID = receiptUUID, productNumber = num, sellingPrice = store.sellingPrice)
              toBuys.addOne(ToBuy(sale, product))
            }
          }
        }

      }

    }
  )

  tableViewBucket.contextMenu = new ContextMenu(
    new MenuItem("delete") {
      onAction = _ => {
        val toBuy = tableViewBucket.getSelectionModel.getSelectedItem
        tableViewBucket.getItems.removeAll(toBuy)
        //        tableViewChoose.getItems.add(toBuy.product)
        val i = products.indexWhere(_.uuid == toBuy.product.uuid)
        if (i != -1) {
          products.update(i, toBuy.product)
        } else {
          products.addOne(toBuy.product)
        }
      }
    }
  )

  val auto = new Button("Auto") {
    onAction = _ => if (field.text.value.length > 2) {
      products.find(_.name.startsWith(field.text.value))
        .orElse(
          products.find(_.name.endsWith(field.text.value))
        ).map { product =>
        tableViewChoose.getSelectionModel.select(product)
      }
    }
  }

  hBox.children = Seq(field, auto)

  tBox.children = Seq(tableViewBucket, tableViewChoose)


  vBox.children = Seq(hBox, tBox)

  dialogPane().content = vBox
  resultConverter = (dialogButton: ButtonType) =>
    if (dialogButton == okButtonType) {
      val items = tableViewBucket.getItems.asScala
      val total = items.map { case ToBuy(sale, _) =>
        sale.productNumber * sale.sellingPrice
      }.sum * 0.2

      val sales = items.map { case ToBuy(sale, _) =>
        sale
      }
      ReceiptMap(Receipt(
        receiptUUID,
        employee.uuid,
        none,
        LocalDateTime.now,
        total,
        0.2
      ), sales.toSeq)
    }
    else null
}
