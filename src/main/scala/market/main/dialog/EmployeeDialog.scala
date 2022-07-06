package market.main.dialog

import market.App.stage
import market.main.category.{Category, CategoryService}
import market.main.employee.Employee
import market.main.employee.Employee.{Position, Positions}
import market.main.product.Product
import market.utils.Validation.TextFieldValidationOps
import scalafx.Includes.{jfxDialogPane2sfx, jfxNode2sfx}
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control._
import scalafx.scene.layout.HBox
import scalafx.scene.text.{Font, Text}

import java.util.UUID
import scala.concurrent.ExecutionContext

class EmployeeDialog(employee: Employee)(implicit val categoryService: CategoryService, implicit val ec: ExecutionContext) extends Dialog[Employee] {
  initOwner(stage)
  title = "Employee Dialog"

  val okButtonType = new ButtonType("OK", ButtonData.OKDone)
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)

  val position = new ChoiceBox[Position]() {
    items = ObservableBuffer(Positions.Cashier, Positions.Manager)
  }


  val salary = new TextField() {
    promptText = "salary"
  }
  salary.consumesOnlyNumbers

  val box = new HBox() {
    spacing = 20
  }
  box.children = Seq(position, salary)

  val okButton = dialogPane().lookupButton(okButtonType)

  //  salary.text.onChange { (_, _, newValue) =>
  //    okButton.disable = newValue.trim().isEmpty
  //  }
  salary.nonEmpty(Seq(okButton))

  dialogPane().content = box

  // Request focus on the username field by default.
  //  Platform.runLater(salary.requestFocus())

  // When the login button is clicked, convert the result to a username-password-pair.
  val conv = (dialogButton: ButtonType) =>
    if (dialogButton == okButtonType)
      employee.copy(position = position.value.value, salary = salary.text.value.toDouble)
    else null
  resultConverter = conv

}