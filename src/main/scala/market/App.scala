package market

import SceneManager.Scenes
import com.typesafe.config.ConfigFactory
import market.main.category.CategoryService
import market.main.customer_card.CustomerCardService
import market.main.employee.{Employee, EmployeeService}
import market.main.product.ProductService
import market.main.receipt.ReceiptService
import market.main.sale.SaleService
import market.main.store_product.StoreProductService
import scalafx.application.JFXApp3
import slick.jdbc.SQLiteProfile.backend.Database
import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import slick.jdbc

object App extends JFXApp3 with ApplicationDependencies {

  override val employee = new ObjectProperty[Employee]()
  override implicit val db: jdbc.SQLiteProfile.backend.Database = Database.forConfig("db", ConfigFactory.load("application.conf"))

  override val employeeService = new EmployeeService

  override val receiptService = new ReceiptService
  override val categoryService = new CategoryService
  override val productService = new ProductService
  override val customerCardService = new CustomerCardService
  override val saleService: SaleService = new SaleService
  override val storeProductService: StoreProductService = new StoreProductService
  //  initDb.get
  //  insertData().get


  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage() {
      title = "Market"
      scene = SceneManager.switchTo(Scenes.Start)
    }
  }

}
