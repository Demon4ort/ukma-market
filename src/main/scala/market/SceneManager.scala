package market

import market.utils.Errors.{ApplicationException, SceneError}
import scalafx.scene.Scene
import scalafxml.core.{FXMLView, NoDependencyResolver}
import scalafx.Includes._

import java.io.IOException
import java.net.URL
import scala.io.Source
import scala.util.{Try, Using}

object SceneManager {

  type SceneType = Scenes.Value

  object Scenes extends Enumeration {
    val Start = Value("Start")
    val Login = Value("Login")
    val SignUp = Value("SignUp")
    val Main = Value("Main")

    implicit final class SceneTypeOps(val sceneType: SceneType) extends AnyVal {
      def location: String = s"/fxml/${sceneType.toString}Form.fxml"
    }

  }

  def resource(fileName: String): Try[URL] = Try {
    Option(getClass.getResource(fileName)).getOrElse(throw new IOException(s"Cannot load resource: $fileName"))
  }


  def switchTo(next: SceneType): Scene = {
    val url = SceneManager.resource(next.location).recoverWith {
      case e: IOException => throw ApplicationException(SceneError(next, e))
    }.get
    new Scene(FXMLView(url, NoDependencyResolver))
  }
}
