package market.main.customer_card

import market.main.credentials.{Address, PhoneNumber}
import market.utils.Entity
import scalafx.beans.property.{ObjectProperty, StringProperty}
import slick.jdbc.SQLiteProfile.api._


case class CustomerCard(uuid: String,
                        surname: String,
                        name: String,
                        patronymic: Option[String],
                        phoneNumber: PhoneNumber,
                        address: Address,
                        percentage: Int) extends Entity {
  val _surname = new StringProperty(this, "surname", surname)
  val _name = new StringProperty(this, "name", name)
  val _patronymic = new StringProperty(this, "patronymic", patronymic.getOrElse(""))
  val _phoneNumber = new ObjectProperty(this, "phone", phoneNumber)
  val _address = new ObjectProperty(this, "address", address)

}

object CustomerCard {

  val customerCards: TableQuery[CustomerCards] = TableQuery[CustomerCards]

  def tupled = (CustomerCard.apply _).tupled

  class CustomerCards(tag: Tag) extends Table[CustomerCard](tag, "Customer_Card") {

    def uuid = column[String]("card_number", O.PrimaryKey)

    def name = column[String]("cust_name")

    def surname = column[String]("cust_surname")

    def patronymic = column[Option[String]]("cust_patronymic")

    def phoneNumber = column[PhoneNumber]("phone_number")

    def city = column[String]("city")

    def street = column[String]("street")

    def index = column[String]("zip_code")

    def address = (city, street, index) <> (Address.tupled, Address.unapply)

    def percentage = column[Int]("percentage")

    override def * = (
      uuid, name,
      surname, patronymic,
      phoneNumber, address, percentage
    ) <> (CustomerCard.tupled, CustomerCard.unapply)
  }
}
