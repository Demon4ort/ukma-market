package market.main.dialog

import market.App.stage
import market.main.customer_card.CustomerCardService
import market.main.employee.Employee
import market.main.product.{Product, ProductService}
import market.main.receipt.{Receipt, ReceiptService}
import market.main.sale.{Sale, SaleService}
import market.main.store_product.StoreProductService
import scalafx.Includes.jfxDialogPane2sfx
import scalafx.application.Platform
import scalafx.beans.property.{ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{Button, ButtonType, ChoiceBox, ContextMenu, Dialog, MenuItem, TableColumn, TableView, TextField}
import scalafx.scene.layout.{HBox, VBox}

import java.util.UUID
import scala.concurrent.ExecutionContext

class BuyDialog(employee: Employee)(
  //  implicit val receiptService: ReceiptService,
  //  implicit val saleService: SaleService,
  implicit val customerCardService: CustomerCardService,
  implicit val productService: ProductService,
  implicit val storeProductService: StoreProductService,
  implicit val ec: ExecutionContext) extends Dialog[(Receipt, Seq[Sale])] {

  initOwner(stage)
  title = "Buy Dialog"

  val okButtonType = new ButtonType("OK", ButtonData.OKDone)
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)

  val hBox = new HBox() {
    spacing = 20
  }
  val vBox = new VBox() {
    spacing = 20
  }

  case class ToBuy(sale: Sale, productName: String) {
    val _name = new StringProperty(this, "name", productName)
    val _price = new ObjectProperty(this, "price", sale.sellingPrice)
  }

  val toBuys = ObservableBuffer.empty[ToBuy]

  val nameChoice = new ChoiceBox[String]() {
    items = products
  }

  val products = ObservableBuffer(productService.all.futureValue.map(_.name): _*)
  val field = new TextField() {
    promptText = "name"
  }
  val auto = new Button("Auto") {
    onAction = _ => if (field.text.value.length > 2) {
      products.find(_.startsWith(field.text.value))
        .orElse(
          products.find(_.endsWith(field.text.value))
        ).map { product =>
        nameChoice.value = product
      }
    }
  }

  vBox.children = Seq(field, auto)


  val tableView = new TableView[ToBuy](toBuys) {
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
      }
    )
  }

  tableView.contextMenu = new ContextMenu(
    new MenuItem("delete") {
      onAction = _ => {
        val a = tableView.getSelectionModel.getSelectedItem
        //        products.
      }
    }
  )

  hBox.children = Seq(vBox, tableView)

  dialogPane().content = hBox
  val conv = (dialogButton: ButtonType) =>
    if (dialogButton == okButtonType) ???
    else null
  resultConverter = conv
}
