package market.login.employee

import market.db.Repository
import market.db.Repository.enumMapper
import market.login.employee.Employee.{Address, PhoneNumber, Position, Positions}
import market.login.employee.EmployeeRepository.{Employees, Query}
import slick.jdbc.SQLiteProfile
import slick.jdbc.SQLiteProfile.api._
import slick.lifted.Rep

import java.time.LocalDateTime
import scala.concurrent.ExecutionContext


class EmployeeRepository(implicit override val ec: ExecutionContext) extends Repository[Employee, Employees, Query] {

  override def findByQuery(query: Query): SQLiteProfile.api.Query[Employees, Employee, Seq] =
    tableQuery.filter(_.uuid === query.uuid)

  override def tableQuery = TableQuery[Employees]
}

object EmployeeRepository {

  case class Query(uuid: String)

  implicit val phoneNumberColumnType: BaseColumnType[PhoneNumber] = MappedColumnType.base[PhoneNumber, String](
    { case PhoneNumber(number) => number },
    { number => PhoneNumber(number) }
  )
  implicit val positionColumnType: BaseColumnType[Position] = enumMapper(Positions)

  class Employees(tag: Tag) extends Table[Employee](tag, "employee") {

    def uuid = column[String]("uuid", O.PrimaryKey)

    def name = column[String]("name")

    def surname = column[String]("surname")

    def patronymic = column[Option[String]]("patronymic")

    def position = column[Position]("position")

    def salary = column[Double]("salary")

    def dateOfBirth = column[LocalDateTime]("dateOfBirth")

    def dateOfStart = column[LocalDateTime]("dateOfStart")

    def phoneNumber = column[PhoneNumber]("phoneNumber")

    def city = column[String]("city")

    def street = column[String]("street")

    def index = column[String]("index")

    def address = (city, street, index) <> (Address.tupled, Address.unapply)

    override def * = (
      uuid, name, surname,
      patronymic, position,
      salary, dateOfBirth,
      dateOfStart, phoneNumber,
      address
    ) <> (Employee.tupled, Employee.unapply)
  }
}
