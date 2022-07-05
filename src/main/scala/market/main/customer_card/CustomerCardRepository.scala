package market.main.customer_card

import market.App
import market.main.customer_card.CustomerCard.CustomerCards
import market.main.customer_card.CustomerCardRepository.Query
import market.utils.Repository
import slick.jdbc.SQLiteProfile.api._
import slick.jdbc.SQLiteProfile

class CustomerCardRepository extends Repository[CustomerCard, CustomerCards, Query] {
  override implicit val db: SQLiteProfile.backend.Database = App.db

  override def tableQuery = TableQuery[CustomerCards]

  override def findByQuery(query: Query): SQLiteProfile.api.Query[CustomerCards, CustomerCard, Seq] = tableQuery.filter(_.uuid === query.uuid)
}

object CustomerCardRepository {
  case class Query(uuid: String)
}
