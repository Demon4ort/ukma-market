package market.login

import cats.implicits.catsSyntaxOptionId
import javafx.fxml.FXML
import scalafx.Includes._
import market.SceneManager.Scenes
import market.login.Alerts.employeeNotFoundAlert
import market.main.employee.EmployeeService
import market.{App, SceneManager}
import scalafx.application.JFXApp3.Stage
import scalafx.application.Platform
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, PasswordField, TextField}
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

@sfxml
class LoginController(@FXML val userName: TextField,
                      @FXML val loginText: Text,
                      @FXML val password: PasswordField,
                      @FXML val passwordText: Text,
                      @FXML val cancel: Button,
                      @FXML val login: Button) {
  lazy val employeeService: EmployeeService = App.employeeService

  App.reset

  def handleCancel(event: ActionEvent): Unit = {
    Stage.setScene(SceneManager.switchTo(Scenes.Start))
  }


  def handleLogin(event: ActionEvent): Unit = {
    def valid = Seq(userName.text.value).forall(_.nonEmpty)

    if (valid) {
      employeeService.logIn(userName.text.value, password.text.value).onComplete {
        case Failure(_) => Platform.runLater(employeeNotFoundAlert.showAndWait())
        case Success(value) => Platform.runLater {
          App.employee.value = value
          Stage.setScene(SceneManager.switchTo(Scenes.Main, value.position.some))
        }
      }
    }
  }
}