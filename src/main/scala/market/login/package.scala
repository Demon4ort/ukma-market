package market

import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonType.{No, OK, Yes}

package object login {

  val UserDoesNotExists = "User does not exists"

  def passwordShouldBeLonger(i: Int = 8) = s"Password should be longer than $i symbols"

  val PasswordsShouldBeIdentical = "Passwords should be identical"

  def lengthCheck(str: String, num: Int = 8): Boolean = str.length > num

  object Alerts {

    def employeeNotFoundAlert: Alert = new Alert(AlertType.Error) {
      title = "Error when logging in"
      headerText = "Can not find such employee account."
      contentText = "Please check the credentials you have used!"
      buttonTypes = Seq(OK)
    }

    def employeeAlreadyExists(login: String): Alert = new Alert(AlertType.Error) {
      title = s"Error when creating employee with login: $login"
      headerText = "Such employee account already exists!"
      contentText = "Please use another login name."
      buttonTypes = Seq(OK)
    }


    def shouldBeYoungerThan18: Alert = new Alert(AlertType.Warning) {
      title = "Wrong age"
      headerText = "You should be older."
      contentText = "Are your age bigger than 18?"
      buttonTypes = Seq(No, Yes)
    }
  }

}
