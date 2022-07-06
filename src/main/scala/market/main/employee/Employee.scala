package market.main.employee

import market.main.employee.Employee.Position
import market.main.credentials.{Address, PhoneNumber}
import market.main.product.Product.Products
import market.utils.Entity
import market.utils.Repository.enumMapper
import scalafx.beans.property.{ObjectProperty, StringProperty}
import slick.jdbc.SQLiteProfile.api._

import java.time.LocalDate
import java.util.UUID

case class Employee(uuid: String = UUID.randomUUID.toString,
                    login: String,
                    password: String,
                    surname: String,
                    name: String,
                    patronymic: Option[String],
                    position: Position,
                    salary: Double,
                    dateOfBirth: LocalDate,
                    dateOfStart: LocalDate,
                    phoneNumber: PhoneNumber,
                    address: Address) extends Entity {
  val _login = new StringProperty(this, "login", login)
  val _surname = new StringProperty(this, "surname", surname)
  val _name = new StringProperty(this, "name", name)
  val _patronymic = new StringProperty(this, "patronymic", patronymic.getOrElse(""))
  val _phoneNumber = new ObjectProperty(this, "date of start", phoneNumber)
  val _address = new ObjectProperty(this, "address", address)
  val _dateOfBirth = new ObjectProperty(this, "date of birth", dateOfBirth)
  val _dateOfStart = new ObjectProperty(this, "phone", dateOfStart)
  val _salary = new ObjectProperty(this, "salary", salary)
  val _position = new ObjectProperty(this, "position", position)
}

object Employee {
  val employees: TableQuery[Employees] = TableQuery[Employees]

  def tupled = (Employee.apply _).tupled

  type Position = Positions.Value

  object Positions extends Enumeration {
    val Unassigned = Value("UNASSIGNED")
    val Cashier = Value("CASHIER")
    val Manager = Value("MANAGER")
  }

  implicit val positionColumnType: BaseColumnType[Position] = enumMapper(Positions)

  class Employees(tag: Tag) extends Table[Employee](tag, "Employee") {

    def login = column[String]("login")

    def password = column[String]("password")

    def uuid = column[String]("id_employee", O.PrimaryKey)

    def name = column[String]("empl_name")

    def surname = column[String]("empl_surname")

    def patronymic = column[Option[String]]("empl_patronymic")

    def position = column[Position]("empl_role")

    def salary = column[Double]("salary")

    def dateOfBirth = column[LocalDate]("date_of_birth")

    def dateOfStart = column[LocalDate]("date_of_start")

    def phoneNumber = column[PhoneNumber]("phone_number")

    def city = column[String]("city")

    def street = column[String]("street")

    def index = column[String]("zip_code")

    def address = (city, street, index) <> (Address.tupled, Address.unapply)

    override def * = (
      uuid, login, password, name,
      surname, patronymic,
      position, salary,
      dateOfBirth, dateOfStart,
      phoneNumber, address
    ) <> (Employee.tupled, Employee.unapply)
  }

}



