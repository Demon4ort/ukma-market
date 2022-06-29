package market.login.employee

import market.login.employee.Employee.Employees
import market.login.employee.EmployeeRepository.Query
import market.utils.Repository
import slick.jdbc.SQLiteProfile
import slick.jdbc.SQLiteProfile.api._


class EmployeeRepository extends Repository[Employee, Employees, Query] {


  override def findByQuery(query: Query): SQLiteProfile.api.Query[Employees, Employee, Seq] =
    tableQuery.filter(_.uuid === query.uuid)

  override def tableQuery = TableQuery[Employees]
}

object EmployeeRepository {

  case class Query(uuid: String)

}
