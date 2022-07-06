package market

import com.typesafe.config.ConfigFactory
import market.main.category.CategoryService
import market.main.customer_card.CustomerCardService
import market.main.employee.{Employee, EmployeeService}
import market.main.product.ProductService
import market.main.receipt.{ReceiptRepository, ReceiptService}
import market.main.sale.SaleService
import market.main.store_product.StoreProductService
import market.utils.FutureFxOps
import scalafx.beans.property.ObjectProperty
import slick.jdbc
import slick.jdbc.SQLActionBuilder
import slick.jdbc.SetParameter.SetUnit

import scala.io.Source
import scala.util.{Try, Using}


trait ApplicationDependencies {

  val db: jdbc.SQLiteProfile.backend.Database
  val employee: ObjectProperty[Employee]
  val employeeService: EmployeeService

  val receiptService: ReceiptService
  val categoryService: CategoryService
  val productService: ProductService
  val customerCardService: CustomerCardService
  val saleService: SaleService
  val storeProductService: StoreProductService

  def reset = {
    employee.value = null
  }


  def runSql(filename: String): Try[Int] = Using(Source.fromResource(filename)) { insertsSqlSource =>
    val sqlActionBuilder = SQLActionBuilder(insertsSqlSource.mkString, SetUnit)
    db.run(sqlActionBuilder.asUpdate).futureValue
  }

  def initDb: Try[Int] = runSql("schema.sql")

  def insertData(): Try[Int] = runSql("data.sql")

}
