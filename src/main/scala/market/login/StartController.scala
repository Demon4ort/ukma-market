package market.login

import market.SceneManager.Scenes
import javafx.fxml.FXML
import market.{App, SceneManager}
import scalafx.event.ActionEvent
import scalafx.scene.control.Button
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.application.JFXApp3.Stage
import scalafx.application.Platform


@sfxml
class StartController(@FXML private val login: Button,
                      @FXML private val signUp: Button) {

  println(App.employee)

  def handleLogin(event: ActionEvent): Unit = {
    Platform.runLater(Stage.setScene(SceneManager.switchTo(Scenes.Login)))
  }

  def handleSignUp(event: ActionEvent): Unit = {
    Stage.setScene(SceneManager.switchTo(Scenes.SignUp))
  }

}
