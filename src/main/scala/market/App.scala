package market

import SceneManager.Scenes
import scalafx.application.JFXApp3
import scalafx.Includes._

object App extends JFXApp3 {
  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage() {
      title = "Market"
      scene = SceneManager.switchTo(Scenes.Start)
    }
  }
}
