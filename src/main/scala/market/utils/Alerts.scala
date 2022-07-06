package market.utils

import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonType.{No, OK, Yes}

object Alerts {

  def areYouSure: Alert = new Alert(AlertType.Confirmation) {
    title = "Confirm"
    headerText = "Are you sure?"
    contentText = "Do you want to do chosen action?"
    buttonTypes = Seq(Yes, No)
  }

  def unknownError: Alert = new Alert(AlertType.Error) {
    title = s"Unknown error"
    headerText = "Error when executing"
    contentText = "Please contact your administrator!"
    buttonTypes = Seq(OK)
  }
}
