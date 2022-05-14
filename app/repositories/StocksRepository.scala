package repositories

import scala.concurrent.Future

final case class StocksDao(
                            id: Int,
                            userId: Int,
                            stockId: String,
                            name: String
                          )

trait StocksRepository {
  def save(stock: StocksDao): Future[Unit]
  def findByStockId(stockId: Int): Future[StocksDao]
}
