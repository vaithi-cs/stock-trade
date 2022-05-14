package repositories

import java.sql.Timestamp

import scala.concurrent.Future

final case class LoginDao(
                           id: Int,
                           username: String,
                           password: String,
                           lastLogin: Timestamp,
                           failedAttempts: Int
                         )

trait LoginRepository {

  def updateLastLogin(lastLogin: Timestamp): Future[Unit]
  def updateFailedAttempts(): Future[Unit]
  def getById(id: String): Future[LoginDao]
  def getByUsername(username: String): Future[LoginDao]
}
