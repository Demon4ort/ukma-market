package market.login

import cats.implicits.{catsSyntaxOptionId, none}
import javafx.fxml.FXML
import market.{App, SceneManager}
import market.utils.Alerts.unknownError
import market.SceneManager.Scenes
import market.login.employee.Employee
import market.login.employee.Employee.{Address, PhoneNumber, Position, Positions}
import market.login.employee.EmployeeService.EmployeeServiceDependency
import market.utils.Errors.UserAlreadyExist
import market.utils.Validation.TextFieldValidationOps
import scalafxml.core.macros.sfxml
import scalafx.Includes._
import scalafx.application.JFXApp3.Stage
import scalafx.application.Platform
import scalafx.beans.property.ObjectProperty
import scalafx.beans.value
import scalafx.collections.ObservableBuffer
import scalafx.event.{ActionEvent, WeakEventHandler}
import scalafx.scene.control.{Button, ButtonType, ChoiceBox, DatePicker, PasswordField, TextField}
import scalafx.scene.text.Text

import java.time.LocalDate
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

@sfxml
class SignUpController(@FXML val name: TextField,
                       @FXML val nameText: Text,
                       @FXML val surname: TextField,
                       @FXML val surnameText: Text,
                       @FXML val login: TextField,
                       @FXML val loginText: Text,
                       @FXML val patronymic: TextField,
                       @FXML val patronymicText: Text,
                       @FXML val password: PasswordField,
                       @FXML val passwordAgain: PasswordField,
                       @FXML val passwordText: Text,
                       @FXML val position: ChoiceBox[Position],
                       @FXML val dateOfBirth: DatePicker,
                       @FXML val phoneNumber: TextField,
                       @FXML val phoneNumberText: Text,
                       @FXML val city: TextField,
                       @FXML val cityText: Text,
                       @FXML val street: TextField,
                       @FXML val streetText: Text,
                       @FXML val index: TextField,
                       @FXML val indexText: Text,
                       @FXML val cancel: Button,
                       @FXML val signUp: Button) extends EmployeeServiceDependency {

  position.items = ObservableBuffer(Positions.values.toSeq: _*)
  position.value = Positions.Cashier

  val textFields: Seq[(TextField, Text)] = Seq(
    login -> loginText,
    name -> nameText,
    surname -> surnameText,
    phoneNumber -> phoneNumberText,
    city -> cityText,
    street -> streetText,
    index -> indexText)

  textFields.map { case (f, t) => f.mustBeFilled(t) }

  login.shouldBeLongerThan(4)(loginText)


  def checkPassword(str: String): Boolean =
    lengthCheck(str) && (str equals password.text.value) && (str equals passwordAgain.text.value)

  def checkPass(str: String): String = str match {
    case _ if !lengthCheck(str) => passwordShouldBeLonger()
    case _ if !(
      (str equals password.text.value) && (str equals passwordAgain.text.value)
      ) => PasswordsShouldBeIdentical
    case _ => ""
  }

  password.text.onChange { (_, _, str) =>
    passwordText.text = checkPass(str)
  }

  passwordAgain.text.onChange { (_, _, str) =>
    passwordText.text = checkPass(str)
  }

  phoneNumber.shouldHaveLength(12)(phoneNumberText)
  phoneNumber.consumesOnlyNumbers

  index.consumesOnlyNumbers

  def validateBirthdate: Boolean = dateOfBirth.value.value isBefore (LocalDate.now minusYears 18)


  def validation: Boolean = {
    textFields.forall { case (f, _) => f.text.value.nonEmpty } &&
      checkPassword(password.text.value) && validateBirthdate &&
      phoneNumber.text.value.length == 12
  }


  def handleCancel(event: ActionEvent): Unit = {
    Stage.setScene(SceneManager.switchTo(Scenes.Start))
  }

  def handleSignUp(event: ActionEvent): Unit = if (validation) {
    val toCreate = Employee(
      uuid = login.text.value,
      password = password.text.value,
      surname = surname.text.value,
      name = name.text.value,
      patronymic = if (patronymic.text.value.isEmpty) none else patronymic.text.value.some,
      position = position.value.value,
      salary = 0,
      dateOfBirth = dateOfBirth.value.value,
      dateOfStart = LocalDate.now,
      phoneNumber = PhoneNumber("+" + phoneNumber.text.value),
      address = Address(city.text.value, street.text.value, index.text.value)
    )
    employeeService.signUp(toCreate).onComplete {
      case Failure(UserAlreadyExist(userName)) => Platform.runLater {
        Alerts.employeeAlreadyExists(userName)
      }
      case Failure(ex) => Platform.runLater {
        ex.printStackTrace()
        unknownError.showAndWait() match {
          case _ => Stage.close()
        }
      }
      case Success(value) => Platform.runLater(Stage.setScene(SceneManager.switchTo(Scenes.Login)))
    }
  } else println(s"Validation: ${password.text.value}")

}
