package market.main.employee

import market.App
import Employee.Employees
import EmployeeRepository.Query
import market.main.employee.Employee.Employees
import market.utils.Repository
import slick.jdbc
import slick.jdbc.SQLiteProfile
import slick.jdbc.SQLiteProfile.api._


class EmployeeRepository extends Repository[Employee, Employees, Query] {


  override def findByQuery(query: Query): SQLiteProfile.api.Query[Employees, Employee, Seq] =
    tableQuery.filter(_.login === query.login)

  override def tableQuery = TableQuery[Employees]

  override implicit val db: jdbc.SQLiteProfile.backend.Database = App.db
}

object EmployeeRepository {

  case class Query(login: String)

}
