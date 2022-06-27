package market.login.employee

import market.login.employee.Employee.{Address, PhoneNumber, Position}

import java.time.LocalDateTime


//  Товар, Категорія.
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
                    surname: String,
                    name: String,
                    patronymic: Option[String],
                    position: Position,
                    salary: Double,
                    dateOfBirth: LocalDateTime,
                    dateOfStart: LocalDateTime,
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

}



