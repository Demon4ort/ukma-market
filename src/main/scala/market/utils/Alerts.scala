package market.utils

import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonType.OK

object Alerts {

  def unknownError: Alert = new Alert(AlertType.Error) {
    title = s"Unknown error"
    headerText = "Error when executing"
    contentText = "Please contact your administrator!"
    buttonTypes = Seq(OK)
  }
}
