package market.main.dialog

import market.App.stage
import scalafx.scene.control._

object Dialogs {
  def category: TextInputDialog = new TextInputDialog() {
    initOwner(stage)
    title = "Category"
    contentText = "Please enter category name:"
  }

}
