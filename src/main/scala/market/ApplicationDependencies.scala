package market

import com.typesafe.config.ConfigFactory
import market.main.employee.{Employee, EmployeeService}
import market.utils.FutureFxOps
import scalafx.beans.property.ObjectProperty
import slick.jdbc
import slick.jdbc.SQLActionBuilder
import slick.jdbc.SQLiteProfile.backend.Database
import slick.jdbc.SetParameter.SetUnit

import scala.io.Source
import scala.util.{Try, Using}


trait ApplicationDependencies {

  val db: jdbc.SQLiteProfile.backend.Database = Database.forConfig("db", ConfigFactory.load("application.conf"))
  val employee: ObjectProperty[Employee]
  val employeeService: EmployeeService


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
