package market

import market.login.employee.{Employee, EmployeeService}
import scalafx.beans.property.ObjectProperty

trait ApplicationDependencies {

  val employee: ObjectProperty[Employee]
  val employeeService: EmployeeService
}
