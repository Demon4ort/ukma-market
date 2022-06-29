package market.login.employee

import market.utils.Repository.enumMapper
import market.login.employee.Employee.{Address, PhoneNumber, Position}

import java.time.{LocalDate, LocalDateTime}
import slick.jdbc.SQLiteProfile.api._


//    Працівник
//  Атрибути працівників:
//  ID_працівника (первинний ключ),
//  ПІБ – складений атрибут (прізвище, ім'я, по батькові),
//  посада,
//  зарплата,
//  дата початку роботи,
//  дата народження,
//  контактний тел.,
//  адреса – складений атрибут (місто, вулиця, індекс).

//  Атрибути товару у магазині: UPC_товару (первинний ключ), ціна продажу, кількість
//  одиниць, акційний товар.

//  Атрибути товару: ID_товару (первинний ключ), назва, виробник, характеристики.

//  Атрибути категорії: номер_категорії(первинний ключ), назва.

//  Атрибути чека: номер_чека (первинний ключ), дата, загальна сума (похідний атрибут,
//  що залежить від кількості куплених одиниць товару та ціни за одиницю цього товару), ПДВ
//  (похідний атрибут, що залежить від загальної суми покупки).

//  Атрибути карти клієнта: номер_карти (первинний ключ), ПІБ – складений атрибут
//  (прізвище, ім'я, по батькові), контактний тел., адреса - необов'язковий, складений атрибут
//  (місто, вулиця, індекс), відсоток.


case class Employee(uuid: String,
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

  def tupled = (Employee.apply _).tupled

  case class PhoneNumber(number: String)

  case class Address(city: String, street: String, index: String)

  type Position = Positions.Value

  object Positions extends Enumeration {
    val Cashier = Value("CASHIER")
    val Manager = Value("MANAGER")
  }

  implicit val phoneNumberColumnType: BaseColumnType[PhoneNumber] = MappedColumnType.base[PhoneNumber, String](
    { case PhoneNumber(number) => number },
    { number => PhoneNumber(number) }
  )
  implicit val positionColumnType: BaseColumnType[Position] = enumMapper(Positions)

  class Employees(tag: Tag) extends Table[Employee](tag, "employee") {

    def uuid = column[String]("uuid", O.PrimaryKey)

    def password = column[String]("password")

    def name = column[String]("name")

    def surname = column[String]("surname")

    def patronymic = column[Option[String]]("patronymic")

    def position = column[Position]("position")

    def salary = column[Double]("salary")

    def dateOfBirth = column[LocalDate]("date_of_birth")

    def dateOfStart = column[LocalDate]("date_of_start")

    def phoneNumber = column[PhoneNumber]("phone_number")

    def city = column[String]("city")

    def street = column[String]("street")

    def index = column[String]("post_index")

    def address = (city, street, index) <> (Address.tupled, Address.unapply)

    override def * = (
      uuid, password, name,
      surname, patronymic,
      position, salary,
      dateOfBirth, dateOfStart,
      phoneNumber, address
    ) <> (Employee.tupled, Employee.unapply)
  }

}



