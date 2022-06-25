package market.login

import market.SceneManager.Scenes
import javafx.fxml.FXML
import market.SceneManager
import scalafx.event.ActionEvent
import scalafx.scene.control.Button
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.application.JFXApp3.Stage


@sfxml
class StartController(@FXML private val login: Button,
                      @FXML private val signUp: Button) {

  def handleLogin(event: ActionEvent): Unit = {
    Stage.setScene(SceneManager.switchTo(Scenes.Login))
  }
  def handleSignUp(event: ActionEvent): Unit = {

  }

}
