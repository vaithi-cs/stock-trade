package repositories

import scala.concurrent.Future

final case class UserAccountDao(
                                 id: Int,
                                 accNo: String,
                                 userId: Int,
                                 balance: Double
                               )

trait UserAccountsRepository {
  def save(userAccount: UserAccountDao): Future[Unit]
  def findByUserId(userId: Int): Future[UserAccountDao]
  def updateBalance(userId: Int, balance: Double): Future[Unit]

}


