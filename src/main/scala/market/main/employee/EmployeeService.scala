package market.main.employee

import market.login.Auth
import EmployeeRepository.Query
import cats.implicits.catsSyntaxOptionId
import market.login
import market.utils.Errors.{ApplicationException, UserAlreadyExist, WrongPasswordError}
import market.utils.Repository.RepositoryOps
import slick.jdbc.SQLiteProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EmployeeService(implicit val db: SQLiteProfile.backend.Database) {

  val repository = new EmployeeRepository
  private val auth: Auth = Auth()

  def all = repository.findBy(Query()).future

  def delete(login: String) = repository.delete(Query(login.some)).future

  def update(e: Employee) = repository.update(Query(login = e.login.some), _.copy(position = e.position, salary = e.salary)).future

  def upsert(e: Employee) = repository.upsert(e).future

  def logIn(login: String, password: String): Future[Employee] = for {
    employee <- repository.findOneBy(Query(login = login.some)).future
    authenticated <- Future(auth.authenticate(password.toCharArray, employee.password))
    logged <- if (authenticated) Future.successful(employee)
    else Future.failed(ApplicationException(WrongPasswordError, Some(s"Can`t log in: $login")))
  } yield logged


  def signUp(employee: Employee): Future[Employee] = for {
    foundOpt <- repository.findOptionBy(Query(employee.uuid.some)).future
    res <- foundOpt match {
      case Some(employee: Employee) => Future.failed(UserAlreadyExist(employee.uuid))
      case None => repository.upsert(employee.copy(password = auth.hash(employee.password.toCharArray))).future
    }
  } yield res

}
