package repositories

import java.sql.Timestamp

import scala.concurrent.Future

final case class OrdersDao(
                            id: Int,
                            userId: Int,
                            tradeId: Int,
                            status: String,
                            created: Timestamp
                          )

trait OrdersRepository{
  def save(ordersDao: OrdersDao): Future[Unit]
  def findByUserId(userId: Int): Future[List[OrdersDao]]
}
