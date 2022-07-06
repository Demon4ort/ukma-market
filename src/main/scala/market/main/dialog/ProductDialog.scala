package market.main.dialog

import cats.implicits.catsSyntaxOptionId
import market.App.stage
import market.main.category.{Category, CategoryService}
import market.main.product.Product
import market.utils.Validation.TextFieldValidationOps
import scalafx.Includes.{jfxDialogPane2sfx, jfxNode2sfx}
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.ButtonBar.ButtonData
import scalafx.scene.control.{Button, ButtonType, ChoiceBox, Dialog, TextArea, TextField}
import scalafx.scene.layout.HBox
import scalafx.scene.text.{Font, Text}

import java.util.UUID
import scala.concurrent.ExecutionContext

class ProductDialog(categories: Seq[Category],
                    product: Option[Product] = None,
                   )(implicit val categoryService: CategoryService, val ec: ExecutionContext) extends Dialog[Product] {
  initOwner(stage)
  title = "Product Dialog"

  val okButtonType = new ButtonType("OK", ButtonData.OKDone)
  dialogPane().buttonTypes = Seq(okButtonType, ButtonType.Cancel)

  val name = new TextField() {
    text = product.map(_.name).getOrElse("")
    promptText = "product name"
  }
  val characteristics = new TextArea() {
    text = product.map(_.name).getOrElse("")
    promptText = "characteristics"
  }

  val categoryBox = new HBox() {
    spacing = 5
  }

  val hint = new Text("Category :") {
    font = Font(size = 15)
  }
  val cats = ObservableBuffer.from(categories.map(_.name))


  val categoryChoice = new ChoiceBox[String]() {
    items = cats
    value = (for {
      categoryUUID <- product.map(_.categoryUUID)
      category <- categories.find(_.uuid == categoryUUID)
    } yield category.name).getOrElse("")
  }


  val categoryButton = new Button("+") {
    onAction = _ => Dialogs.category.showAndWait() match {
      case Some(n) => for {
        cat <- categoryService.add(Category(name = n))
        _ = Platform.runLater {
          cats.add(cat.name)
          categoryChoice.value = cat.name
        }
      } yield cat
      case None => ()
    }
  }

  categoryBox.children = Seq(categoryChoice, categoryButton)

  val box = new HBox() {
    spacing = 20
  }
  box.children = Seq(name, hint, categoryBox, characteristics)

  val okButton = dialogPane().lookupButton(okButtonType)

  characteristics.text.onChange { (_, _, newValue) =>
    okButton.disable = newValue.trim().isEmpty
  }
  name.nonEmpty(Seq(okButton))

  dialogPane().content = box

  // Request focus on the username field by default.
  Platform.runLater(name.requestFocus())

  // When the login button is clicked, convert the result to a username-password-pair.
  val conv = (dialogButton: ButtonType) =>
    if (dialogButton == okButtonType)
      new Product(
        product.map(_.uuid).getOrElse(UUID.randomUUID.toString),
        categories.find(c =>
          c.name == categoryChoice.value.value
        ).head.uuid,
        name.text(),
        characteristics.text())
    else null
  resultConverter = conv

}