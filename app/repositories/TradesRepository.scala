package repositories

import java.sql.Timestamp

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import repositories.OrdersRepository.orders
import slick.lifted.Tag
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{TableQuery, Tag}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

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

class TradesDaoTable(tag: Tag) extends Table[TradesDao](tag, "trade") {

  def id = column[Int]("id")
  def userId = column[Int]("userId")
  def userAccountId = column[Int]("userAccountId")
  def stockId = column[Int]("stockId")
  def quantity = column[Int]("quantity")
  def price = column[Double]("price")
  def timestamp = column[Timestamp]("timestamp")

  def currencyType = column[String]("currencyType")

  override def * =
    (id, userId, userAccountId, stockId, quantity, price, timestamp, currencyType) <> (TradesDao.tupled, TradesDao.unapply)

}

object TradesRepository {
  val trades = TableQuery[TradesDaoTable]

  class Trade @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                        (implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile]
      with TradesRepository {
    override def save(tradesDao: TradesDao): Future[Unit] =
      db.run(trades += tradesDao).map(_ => ())

    override def findByUserId(userId: Int): Future[List[TradesDao]] =
      db.run(trades.filter(_.userId === userId).result).map(_.toList)
  }
}
