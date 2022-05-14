package repositories

import java.sql.Timestamp

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import repositories.LoginRepository.logins
import slick.lifted.Tag
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.{TableQuery, Tag}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

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

class OrderDaoTable(tag: Tag) extends Table[OrdersDao](tag, "orders") {

  def id = column[Int]("id")
  def userId = column[Int]("userId")
  def tradeId = column[Int]("tradeId")

  def status = column[String]("status")
  def created = column[Timestamp]("created")

  override def * =
    (id, userId, tradeId, status, created) <> (OrdersDao.tupled, OrdersDao.unapply)

}

object OrdersRepository {
  val orders = TableQuery[OrderDaoTable]

  class Order @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                       (implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile]
      with OrdersRepository {
    override def save(ordersDao: OrdersDao): Future[Unit] = {
      db.run(orders += ordersDao).map(_ => ())
    }

    override def findByUserId(userId: Int): Future[List[OrdersDao]] =
      db.run(orders.filter(_.userId === userId).result).map(_.toList)
  }
}
