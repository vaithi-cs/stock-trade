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

final case class UserAccountDao(
                                 id: Int,
                                 accNo: String,
                                 userId: Int,
                                 balance: Double
                               )

trait UserAccountsRepository {
  def save(userAccount: UserAccountDao): Future[Unit]
  def findByUserId(userId: Int): Future[Option[UserAccountDao]]
  def updateBalance(userId: Int, balance: Double): Future[Unit]
}

class UserAccountDaoTable(tag: Tag) extends Table[UserAccountDao](tag, "useraccount") {

  def id = column[Int]("id")
  def accNo = column[String]("accNo")
  def userId = column[Int]("userId")

  def balance = column[Double]("balance")

  override def * =
    (id, accNo, userId, balance) <> (UserAccountDao.tupled, UserAccountDao.unapply)
}

object UserAccountsRepository {
  val userAccounts = TableQuery[UserAccountDaoTable]

  class UserAccount @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
                        (implicit executionContext: ExecutionContext)
    extends HasDatabaseConfigProvider[JdbcProfile]
      with UserAccountsRepository {
    override def save(userAccount: UserAccountDao): Future[Unit] =
      db.run(userAccounts += userAccount).map(_ => ())

    override def findByUserId(userId: Int): Future[Option[UserAccountDao]] =
      db.run(userAccounts.filter(_.userId === userId).result.headOption)

    override def updateBalance(userId: Int, balance: Double): Future[Unit] = {
      val updateQuery = userAccounts.filter(_.userId === userId)
        .map(_.balance)
      db.run(updateQuery.update(balance))
        .map(_ => ())
    }
  }
}


