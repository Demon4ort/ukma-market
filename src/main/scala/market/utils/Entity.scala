package market.utils


trait Entity

object ManagerCreationEntities extends Enumeration {
  type EntityType = Value
  val Employee, Sale, Product, Receipt, CustomerCard, Category, StoreProduct = Value
  //  val StoreProduct = Value
}
