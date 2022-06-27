package market

import scalafx.scene.Scene
import scalafxml.core.{FXMLView, NoDependencyResolver}
import scalafx.Includes._

import java.io.IOException
import java.net.URL

object SceneManager {

  type SceneType = Scenes.Value
  object Scenes extends Enumeration {
    val Start = Value("/fxml/login/StartForm.fxml")
    val Login = Value("/fxml/login/LoginForm.fxml")
    //    val SignUp = Value(???)
  }

  def resource(fileName: String): URL = Option(getClass.getResource(fileName)).getOrElse(throw new IOException(s"Cannot load resource: $fileName"))


  def switchTo(next: SceneType) = new Scene(FXMLView(SceneManager.resource(next.toString), NoDependencyResolver))
}
