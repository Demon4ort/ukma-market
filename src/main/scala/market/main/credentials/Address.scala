package market.main.credentials

case class Address(city: String, street: String, index: String) {
  override def toString = s"$city, $street, $index"
}
