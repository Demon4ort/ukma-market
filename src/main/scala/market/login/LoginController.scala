package market.login

import javafx.fxml.FXML
import market.SceneManager
import market.SceneManager.Scenes
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, PasswordField, TextField}
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.application.JFXApp3.Stage

@sfxml
class LoginController(@FXML val userName: TextField,
                      @FXML val password: PasswordField,
                      @FXML val cancel: Button,
                      @FXML val login: Button) {

  def handleCancel(event: ActionEvent): Unit = {
    Stage.setScene(SceneManager.switchTo(Scenes.Start))
  }

  def handleLogin(event: ActionEvent): Unit = {

  }

}
