package repositories

import java.sql.Timestamp

import scala.concurrent.Future

final case class TradesDao(
                          id: Int,
                          userId: Int,
                          userAccountId: Int,
                          stockId: Int,
                          quantity: Int,
                          price: Double,
                          timestamp: Timestamp,
                          currencyType: String
                          )


trait TradesRepository {
  def save(tradesDao: TradesDao): Future[Unit]
  def findByUserId(userId: Int): Future[List[TradesDao]]
}
