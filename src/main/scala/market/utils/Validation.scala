package market.utils

import scalafx.event.subscriptions.Subscription
import scalafx.scene.Node
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.text.Text

object Validation {

  val mustBeFilledError = "Must be filled"

  def shouldBeLongerThanError(n: Int) = s"Should be longer than $n"

  def shouldHaveLengthError(n: Int) = s"Should have length $n"

  def longerThan(str: String, num: Int = 8): Boolean = str.length > num

  final implicit class TextFieldValidationOps(val field: TextField) extends AnyVal {

    def shouldBeLongerThan(n: Int)(indicator: Text): Subscription = field.text.onChange { (_, _, str) =>
      if (str.length > n) indicator.text = ""
      else indicator.text = shouldBeLongerThanError(n)
    }

    def mustBeFilled(indicator: Text): Subscription = field.text.onChange { (_, _, str) =>
      if (str.nonEmpty) indicator.text = ""
      else indicator.text = mustBeFilledError
    }

    def shouldHaveLength(n: Int)(indicator: Text): Subscription = field.text.onChange { (_, _, str) =>
      if (str.length == n) indicator.text = ""
      else indicator.text = shouldHaveLengthError(n)
    }

    def consumesOnlyNumbers = field.text.onChange { (_, old, str) =>
      if (!str.matches("\\d+")) {
        field.text.value = old
      }
    }

    def nonEmpty(nodes: Seq[Node]) = {
      nodes.foreach(_.disable = true)
      field.text.onChange { (_, _, newValue) =>
        nodes.foreach(_.disable = newValue.trim().isEmpty)
      }
    }

  }
}
