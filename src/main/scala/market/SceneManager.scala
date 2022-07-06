package market

import market.main.employee.Employee.Positions.{Cashier, Manager}
import market.main.employee.Employee.{Position, Positions}
import market.utils.Alerts
import market.utils.Errors.{ApplicationException, SceneError}
import scalafx.Includes._
import scalafx.scene.Scene
import scalafxml.core.{FXMLView, NoDependencyResolver}
import java.io.IOException
import java.net.URL
import scala.util.Try

object SceneManager {

  type SceneType = Scenes.Value

  object Scenes extends Enumeration {
    val Start = Value("Start")
    val Login = Value("Login")
    val SignUp = Value("SignUp")
    val Main = Value("Main")
    val MainCashier = Value("MainCashier")

    implicit final class SceneTypeOps(val sceneType: SceneType) extends AnyVal {
      def location: String = s"/fxml/${sceneType.toString}Form.fxml"
    }

  }

  def resource(fileName: String): Try[URL] = Try {
    Option(getClass.getResource(fileName)).getOrElse(throw new IOException(s"Cannot load resource: $fileName"))
  }


  def switchTo(next: SceneType, position: Option[Position] = None): Scene = {
    val n = next match {
      case Scenes.Main =>
        val pos = position.getOrElse {
          Alerts.unknownError.showAndWait()
          Positions.Unassigned
        }
        pos match {
          case Cashier => Scenes.MainCashier
          case Manager => Scenes.Main
        }
      case other => other
    }
    val url = SceneManager.resource(n.location).recoverWith {
      case e: IOException => throw ApplicationException(SceneError(n, e))
    }.get
    new Scene(FXMLView(url, NoDependencyResolver))
  }
}
