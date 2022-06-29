package market.utils

import market.SceneManager.SceneType

import java.io.IOException

object Errors {

  trait ApplicationError extends Throwable

  case class ApplicationException(error: ApplicationError, message: Option[String] = None) extends Exception

  case class SceneError(scene: SceneType, ex: IOException) extends ApplicationError

  case class DatabaseError(message: String) extends ApplicationError

  case object WrongPasswordError extends ApplicationError

  case class UserAlreadyExist(userName: String) extends ApplicationError

  object SceneError {
    def notAbleToLoadSceneError(scene: SceneType) = s"Cannot load scene $scene"
  }

}
