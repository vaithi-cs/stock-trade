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

final case class LoginDao(
  id: Int,
  username: String,
  password: String,
  lastLogin: Timestamp,
  failedAttempts: Int
)

trait LoginRepository {

  def updateLastLogin(id: Int, lastLogin: Timestamp): Future[Unit]
  def updateFailedAttempts(id: Int): Future[Unit]
  def getById(id: Int): Future[Option[LoginDao]]
  def getByUsername(username: String): Future[Option[LoginDao]]
}

class LoginDaoTable (tag: Tag) extends Table[LoginDao](tag, "user") {

  def id = column[Int]("id")

  def username = column[String]("username")
  def password = column[String]("password")
  def lastLogin = column[Timestamp]("lastlogin")
  def failedAttempts = column[Int]("failedAttempts")

  override def * =
    (id, username, password, lastLogin, failedAttempts) <> (LoginDao.tupled, LoginDao.unapply)

}

object LoginRepository {

  val logins = TableQuery[LoginDaoTable]

  class Login @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)
  (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile]
    with LoginRepository {
    override def updateLastLogin(id: Int, lastLogin: Timestamp): Future[Unit] = {
      val updateQuery = logins.filter(_.id === id)
        .map(_.lastLogin)
      db.run(updateQuery.update(lastLogin))
        .map(_ => ())
    }

    override def updateFailedAttempts(id: Int): Future[Unit] = {
      val getQuery = logins.filter(_.id ===id)
        .map(_.failedAttempts)
      val updateQuery = logins.filter(_.id === id)
        .map(_.failedAttempts)
      val actions = for {
        current <- getQuery.result
        _ <- updateQuery.update(current.head)
      } yield ()
      db.run(actions)
        .map(_ => ())
    }

    override def getById(id: Int): Future[Option[LoginDao]] =
      db.run(logins.filter(_.id === id).result.headOption)

    override def getByUsername(username: String): Future[Option[LoginDao]] =
      db.run(logins.filter(_.username === username).result.headOption)
  }
}
