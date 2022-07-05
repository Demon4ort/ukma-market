package market.main.employee

import market.main.employee.Employee.Position
import market.main.credentials.{Address, PhoneNumber}
import market.main.product.Product.Products
import market.utils.Repository.enumMapper
import slick.jdbc.SQLiteProfile.api._

import java.time.LocalDate

case class Employee(uuid: String,
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
                    address: Address)

object Employee {
  val employees: TableQuery[Employees] = TableQuery[Employees]

  def tupled = (Employee.apply _).tupled

  type Position = Positions.Value

  object Positions extends Enumeration {
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



