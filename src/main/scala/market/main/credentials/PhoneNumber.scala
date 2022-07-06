package market.main.credentials

import slick.jdbc.SQLiteProfile.api._

case class PhoneNumber(number: String) {
  require(number.length == 12)

  override def toString = s"+$number"
}

object PhoneNumber {

  implicit val phoneNumberColumnType: BaseColumnType[PhoneNumber] = MappedColumnType.base[PhoneNumber, String](
    { case PhoneNumber(number) => number },
    { number => PhoneNumber(number) }
  )
}