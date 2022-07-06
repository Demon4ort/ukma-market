package market.main.receipt


import market.main.customer_card.CustomerCard
import market.main.employee.Employee
import market.utils.Entity
import scalafx.beans.property.{DoubleProperty, ObjectProperty, Property, StringProperty}
import slick.jdbc.SQLiteProfile.api._

import java.time.LocalDateTime
import java.time.chrono.ChronoLocalDateTime

case class Receipt(uuid: String,
                   employeeUUID: String,
                   cardUUID: Option[String],
                   creationDate: LocalDateTime,
                   totalSum: Double,
                   pdv: Double = 20) extends Entity {
  val _uuid = new StringProperty(this, "check number", uuid)
  val _employeeUUID = new StringProperty(this, "employee id", employeeUUID)
  val _creationDate = new ObjectProperty(this, "print date", creationDate)
  val _totalSum = new ObjectProperty(this, "total sum", totalSum)
  val _pdv = new ObjectProperty(this, "PDV", pdv)
}

object Receipt {

  val receipts: TableQuery[Receipts] = TableQuery[Receipts]

  def tupled = (Receipt.apply _).tupled

  class Receipts(tag: Tag) extends Table[Receipt](tag, "Receipt") {

    def uuid = column[String]("check_number")

    def employeeUUID = column[String]("id_employee")

    def cardUUID = column[Option[String]]("card_number")

    def creationDate = column[LocalDateTime]("print_date")

    def totalSum = column[Double]("sum_total")

    def pdv = column[Double]("pdv")


    def employeeUUID_fk =
      foreignKey("pass_fk", employeeUUID,
        Employee.employees)(_.uuid,
        onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.NoAction)

    def cardUUID_fk =
      foreignKey("room_fk", cardUUID,
        CustomerCard.customerCards)(_.uuid,
        onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.NoAction)


    override def * = (uuid, employeeUUID, cardUUID, creationDate, totalSum, pdv) <> (Receipt.tupled, Receipt.unapply)
  }
}
