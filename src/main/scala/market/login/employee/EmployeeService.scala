package market.login.employee

import cats.implicits.catsSyntaxOptionId
import market.login.Auth
import market.login.employee.Employee.{Address, PhoneNumber, Positions}
import market.login.employee.EmployeeRepository.Query
import market.utils.Errors.{ApplicationException, UserAlreadyExist, WrongPasswordError}
import market.utils.FutureFxOps
import market.utils.Repository.RepositoryOps
import slick.jdbc.SQLiteProfile

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EmployeeService {

  private val repository = new EmployeeRepository
  private implicit val db: SQLiteProfile.backend.Database = repository.db
  private val auth: Auth = Auth()

  repository.init.future.futureValue
  val admin: Employee = Employee(
    "adminMaster",
    auth.hash("test12345".toCharArray),
    "admin",
    "admin",
    "admin".some,
    Positions.Manager,
    100.0,
    LocalDate.now minusYears 20,
    LocalDate.now,
    PhoneNumber("+380639336969"),
    Address("Lviv", "Admin", "70000")
  )
  repository.upsert(admin).future.futureValue


  def logIn(login: String, password: String): Future[Employee] = for {
    employee <- repository.findOneBy(Query(uuid = login)).future
    authenticated <- Future(auth.authenticate(password.toCharArray, employee.password))
    logged <- if (authenticated) Future.successful(employee)
    else Future.failed(ApplicationException(WrongPasswordError, Some(s"Can`t log in: $login")))
  } yield logged


  def signUp(employee: Employee): Future[Employee] = for {
    foundOpt <- repository.findOptionBy(Query(employee.uuid)).future
    res <- foundOpt match {
      case Some(employee: Employee) => Future.failed(UserAlreadyExist(employee.uuid))
      case None => repository.upsert(employee.copy(password = auth.hash(employee.password.toCharArray))).future
    }
  } yield res

}

object EmployeeService {

  import market.App

  trait EmployeeServiceDependency {

    val employeeService: EmployeeService = App.employeeService

  }
}
