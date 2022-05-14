package repositories

import java.sql.Timestamp

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.lifted.Tag
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{TableQuery, Tag}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

final case class StocksDao(
                            id: Int,
                            userId: Int,
                            stockId: Int,
                            name: String
                          )

trait StocksRepository {
  def save(stock: StocksDao): Future[Unit]
  def findByStockId(stockId: Int): Future[Option[StocksDao]]
}

class StocksDaoTable(tag: Tag) extends Table[StocksDao](tag, "stocks") {

  def id = column[Int]("id")
  def userId = column[Int]("userId")
  def stockId = column[Int]("stockId")

  def name = column[String]("name")

  override def * =
    (id, userId, stockId, name) <> (StocksDao.tupled, StocksDao.unapply)

}

object StocksRepository {
  val stocks = TableQuery[StocksDaoTable]

  class Stock @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                        (implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile]
      with StocksRepository {
    override def save(stock: StocksDao): Future[Unit] = {
      db.run(stocks += stock).map(_ => ())
    }

    override def findByStockId(stockId: Int): Future[Option[StocksDao]] =
      db.run(stocks.filter(_.stockId === stockId).result.headOption)
  }
}