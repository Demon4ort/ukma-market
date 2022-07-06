package market.main

import javafx.fxml.FXML
import market.SceneManager.Scenes
import market.main.category.{Category, CategoryService}
import market.main.credentials.{Address, PhoneNumber}
import market.main.customer_card.{CustomerCard, CustomerCardService}
import market.main.dialog.{BuyDialog, Dialogs, EmployeeDialog, ProductDialog}
import market.main.employee.Employee.Position
import market.main.employee.{Employee, EmployeeService}
import market.main.product.ProductService
import market.main.product.Product
import market.main.receipt.{Receipt, ReceiptService}
import market.main.sale.{Sale, SaleService}
import market.main.store_product.{StoreProduct, StoreProductService}
import market.utils.Alerts.areYouSure
import market.utils.ManagerCreationEntities.EntityType
import market.utils.{FutureFxOps, ManagerCreationEntities}
import market.{App, SceneManager}
import scalafx.application.JFXApp3.Stage
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.scene.control._
import scalafx.scene.layout.Pane
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.beans.property.StringProperty

import java.time.{LocalDate, LocalDateTime}
import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@sfxml
class MainController(@FXML val menuBar: MenuBar,
                     @FXML val pane: Pane,
                     @FXML val logOut: Button,
                     @FXML val delete: Button,
                     @FXML val edit: Button,
                     @FXML val add: Button,
                     @FXML val choiceBox: ChoiceBox[EntityType],
                     @FXML val userNameText: Text,
                     @FXML val errorCode: Text
                    ) {

  implicit lazy val receiptService: ReceiptService = App.receiptService
  implicit lazy val categoryService: CategoryService = App.categoryService
  implicit lazy val productService: ProductService = App.productService
  implicit lazy val employeeService: EmployeeService = App.employeeService
  implicit lazy val customerCardService: CustomerCardService = App.customerCardService
  implicit lazy val saleService: SaleService = App.saleService
  implicit lazy val storeProductService: StoreProductService = App.storeProductService

  userNameText.text <== StringProperty(App.employee.value.login)
  choiceBox.items = ObservableBuffer(ManagerCreationEntities.values.toSeq: _*)

  def receiptsBuffer: ObservableBuffer[Receipt] = ObservableBuffer(receiptService.all.futureValue: _*)

  def categoriesBuffer: ObservableBuffer[Category] = ObservableBuffer(categoryService.all.futureValue: _*)

  def productsBuffer: ObservableBuffer[Product] = ObservableBuffer(productService.all.futureValue: _*)

  def employeesBuffer: ObservableBuffer[Employee] = ObservableBuffer(employeeService.all.futureValue: _*)

  def customerCardsBuffer: ObservableBuffer[CustomerCard] = ObservableBuffer(customerCardService.all.futureValue: _*)

  def salesBuffer: ObservableBuffer[Sale] = ObservableBuffer(saleService.all.futureValue: _*)

  def storeProductsBuffer: ObservableBuffer[StoreProduct] = ObservableBuffer(storeProductService.all.futureValue: _*)


  def deleteItem(run: => Unit) = new MenuItem("delete") {
    onAction = _ => Platform.runLater {
      areYouSure.showAndWait() match {
        case Some(ButtonType.Yes) => run
        case _ => receipts.getSelectionModel.clearSelection()
      }
    }
  }

  lazy val storeProducts = new TableView[StoreProduct](storeProductsBuffer) {
    prefWidth = 600
    prefHeight = 300
    columns ++= Seq(
      new TableColumn[StoreProduct, Double]() {
        text = "selling price"
        cellValueFactory = _.value._sellingPrice
      },
      new TableColumn[StoreProduct, Int]() {
        text = "products number"
        cellValueFactory = _.value._productsNumber
      },
      new TableColumn[StoreProduct, Boolean]() {
        text = "promotional product"
        cellValueFactory = _.value._promotionalProduct
      }
    )
  }

  lazy val sales = new TableView[Sale](salesBuffer) {
    prefWidth = 300
    prefHeight = 300
    columns ++= Seq(
      new TableColumn[Sale, Double]() {
        text = "selling price"
        cellValueFactory = _.value._sellingPrice
      },
      new TableColumn[Sale, Int]() {
        text = "product number"
        cellValueFactory = _.value._productNumber
      }
    )
  }

  lazy val customerCards = new TableView[CustomerCard](customerCardsBuffer) {
    prefWidth = 600
    prefHeight = 300
    columns ++= Seq(
      new TableColumn[CustomerCard, String]() {
        text = "surname"
        cellValueFactory = _.value._surname
      },
      new TableColumn[CustomerCard, String]() {
        text = "name"
        cellValueFactory = _.value._name
      },
      new TableColumn[CustomerCard, String]() {
        text = "patronymic"
        cellValueFactory = _.value._patronymic
      },
      new TableColumn[CustomerCard, PhoneNumber]() {
        text = "phone"
        cellValueFactory = _.value._phoneNumber
      },
      new TableColumn[CustomerCard, Address]() {
        text = "address"
        cellValueFactory = _.value._address
      }
    )
  }

  lazy val employees = new TableView[Employee](employeesBuffer) {
    prefWidth = 700
    prefHeight = 300
    columns ++= Seq(
      new TableColumn[Employee, String]() {
        text = "login"
        cellValueFactory = _.value._login
      },
      new TableColumn[Employee, String]() {
        text = "surname"
        cellValueFactory = _.value._surname
      },
      new TableColumn[Employee, String]() {
        text = "name"
        cellValueFactory = _.value._name
      },
      new TableColumn[Employee, String]() {
        text = "patronymic"
        cellValueFactory = _.value._patronymic
      },
      new TableColumn[Employee, LocalDate]() {
        text = "date of birth"
        cellValueFactory = _.value._dateOfBirth
      },
      new TableColumn[Employee, PhoneNumber]() {
        text = "phone"
        cellValueFactory = _.value._phoneNumber
      },
      new TableColumn[Employee, LocalDate]() {
        text = "date of start"
        cellValueFactory = _.value._dateOfStart
      },
      new TableColumn[Employee, Double]() {
        text = "salary"
        cellValueFactory = _.value._salary
      },
      new TableColumn[Employee, Address]() {
        text = "address"
        cellValueFactory = _.value._address
      },
      new TableColumn[Employee, Position]() {
        text = "position"
        cellValueFactory = _.value._position
      }
    )

  }

  lazy val receipts: TableView[Receipt] = new TableView[Receipt](receiptsBuffer) {
    prefWidth = 470
    prefHeight = 300
    columns ++= Seq(
      new TableColumn[Receipt, String]() {
        text = "check number"
        cellValueFactory = _.value._uuid
      },
      new TableColumn[Receipt, LocalDateTime]() {
        text = "print date"
        cellValueFactory = _.value._creationDate
      },
      new TableColumn[Receipt, Double]() {
        text = "total sum"
        cellValueFactory = _.value._totalSum
      },
      new TableColumn[Receipt, Double]() {
        text = "pdv"
        cellValueFactory = _.value._pdv
      }
    )
  }

  lazy val products: TableView[Product] = new TableView[Product](productsBuffer) {
    prefWidth = 400
    prefHeight = 300
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
  lazy val categories: TableView[Category] = new TableView[Category](categoriesBuffer) {
    prefWidth = 120
    prefHeight = 300
    columns ++= Seq(new TableColumn[Category, String]() {
      text = "category name"
      cellValueFactory = _.value._name
    }
    )
  }

  def receiptDelete = {
    receiptService.delete(receipts.getSelectionModel.getSelectedItem.uuid).futureValue
    receipts.items.value = receiptsBuffer
  }

  def productDelete = {
    productService.delete(products.getSelectionModel.getSelectedItem.uuid).futureValue
    products.items.value = productsBuffer
  }

  def categoryDelete = {
    categoryService.delete(categories.getSelectionModel.getSelectedItem.uuid).futureValue
    categories.items.value = categoriesBuffer
  }

  def employeeDelete = {
    employeeService.delete(employees.getSelectionModel.getSelectedItem.uuid).futureValue
    employees.items.value = employeesBuffer
  }

  def customerCardsDelete = {
    customerCardService.delete(customerCards.getSelectionModel.getSelectedItem.uuid).futureValue
    customerCards.items.value = customerCardsBuffer
  }

  def salesDelete = {
    saleService.delete(sales.getSelectionModel.getSelectedItem.uuid).futureValue
    sales.items.value = salesBuffer
  }

  def storeProductsDelete = {
    storeProductService.delete(storeProducts.getSelectionModel.getSelectedItem.uuid).futureValue
    storeProducts.items.value = storeProductsBuffer
  }


  def updateEmployee = Platform.runLater {
    Option(employees.getSelectionModel.getSelectedItem).map { employee =>
      new EmployeeDialog(employee).showAndWait() match {
        case Some(value: Employee) =>
          println(value)
          employeeService.update(value).futureValue
          choiceBox.value = ManagerCreationEntities.Employee
        case None => ()
      }
    }

    //        choiceBox.value = ManagerCreationEntities.Employee

  }

  employees.contextMenu = new ContextMenu(
    deleteItem(employeeDelete),
    new MenuItem("addInfo") {
      onAction = _ => updateEmployee
    }
  )
  receipts.contextMenu = new ContextMenu(
    deleteItem(receiptDelete),
    new MenuItem("employee") {
      onAction = _ => Platform.runLater {
        val employeeUUID = receipts.getSelectionModel.getSelectedItem.employeeUUID
        choiceBox.value = ManagerCreationEntities.Employee
        employees.items = employees.items.value.filter(_.uuid == employeeUUID)
      }
    }
  )
  products.contextMenu = new ContextMenu(
    deleteItem(productDelete),
    new MenuItem("category") {
      onAction = _ => Platform.runLater {
        val categoryUUID = products.getSelectionModel.getSelectedItem.categoryUUID
        choiceBox.value = ManagerCreationEntities.Category
        categories.items = categories.items.value.filter(_.uuid == categoryUUID)
      }
    }
  )
  categories.contextMenu = new ContextMenu(
    deleteItem(categoryDelete),
    new MenuItem("products") {
      onAction = _ => Platform.runLater {
        val categoryUUID = categories.getSelectionModel.getSelectedItem.uuid
        choiceBox.value = ManagerCreationEntities.Product
        products.items = products.items.value.filter(_.uuid == categoryUUID)
      }
    }
  )

  customerCards.contextMenu = new ContextMenu(deleteItem(customerCardsDelete))
  sales.contextMenu = new ContextMenu(
    deleteItem(salesDelete),
    new MenuItem("storeProduct") {
      onAction = _ => Platform.runLater {
        val storeProductUUID = sales.getSelectionModel.getSelectedItem.uuid
        choiceBox.value = ManagerCreationEntities.StoreProduct
        storeProducts.items = storeProducts.items.value.filter(_.uuid == storeProductUUID)
      }
    },
    new MenuItem("receipt") {
      onAction = _ => Platform.runLater {
        val receiptUUID = sales.getSelectionModel.getSelectedItem.receiptUUID
        choiceBox.value = ManagerCreationEntities.Receipt
        receipts.items = receipts.items.value.filter(_.uuid == receiptUUID)
      }
    }

  )
  storeProducts.contextMenu = new ContextMenu(
    deleteItem(storeProductsDelete),
    new MenuItem("promotion") {
      onAction = _ => Platform.runLater {
        val receiptUUID = storeProducts.getSelectionModel.getSelectedItem.promUUID
        receiptUUID.map { uuid =>
          choiceBox.value = ManagerCreationEntities.StoreProduct
          storeProducts.items = storeProducts.items.value.filter(_.uuid == uuid)
        }

      }
    },
    new MenuItem("product") {
      onAction = _ => Platform.runLater {
        val productUUID = storeProducts.getSelectionModel.getSelectedItem.productUUID
        choiceBox.value = ManagerCreationEntities.Product
        products.items = products.items.value.filter(_.uuid == productUUID)
      }
    }
  )


  choiceBox.value.onChange { (_, _, value) =>
    if (value == ManagerCreationEntities.Employee) add.disable = true
    else add.disable = false
    value match {
      case ManagerCreationEntities.Employee =>
        employees.items = employeesBuffer
        pane.children = employees

      case ManagerCreationEntities.Sale =>
        sales.items = salesBuffer
        pane.children = sales

      case ManagerCreationEntities.Product =>
        products.items = productsBuffer
        pane.children = products

      case ManagerCreationEntities.Receipt =>
        receipts.items = receiptsBuffer
        pane.children = receipts

      case ManagerCreationEntities.CustomerCard =>
        customerCards.items = customerCardsBuffer
        pane.children = customerCards

      case ManagerCreationEntities.Category =>
        categories.items = categoriesBuffer
        pane.children = categories

      case ManagerCreationEntities.StoreProduct =>
        storeProducts.items = storeProductsBuffer
        pane.children = storeProducts
    }

  }

  def addHandle(event: ActionEvent): Unit = Platform.runLater {
    println("adding")
    choiceBox.value.value match {
      case market.utils.ManagerCreationEntities.Sale | ManagerCreationEntities.Receipt =>
        new BuyDialog(App.employee.value).showAndWait() match {
          case Some((receipt: Receipt, seq: Seq[Sale])) => Platform.runLater {
            for {
              _ <- receiptService.upsert(receipt)
              _ <- Future.traverse(seq)(saleService.upsert)
            } yield ()
          }
          case None => ()
        }
      case ManagerCreationEntities.Product => new ProductDialog(categoryService.all.futureValue).showAndWait() match {
        case Some(value: Product) => productService.upsert(value)
        case None => ()
      }
      case market.utils.ManagerCreationEntities.CustomerCard => ()
      case ManagerCreationEntities.Category => Dialogs.category.showAndWait() match {
        case Some(value) => categoryService.add(Category(UUID.randomUUID.toString, value))
        case None => ()
      }
      case _ => ()
    }
  }


  def logOutHandle(event: ActionEvent): Unit = {
    App.reset
    Stage.setScene(SceneManager.switchTo(Scenes.Start))
  }


}
