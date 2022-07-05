package market

import SceneManager.Scenes
import market.main.employee.{Employee, EmployeeService}
import scalafx.application.JFXApp3
import scalafx.Includes._
import scalafx.beans.property.ObjectProperty

object App extends JFXApp3 with ApplicationDependencies {

  override lazy val employee = new ObjectProperty[Employee]()
  override lazy val employeeService = new EmployeeService
  //  initDb.get
  //  insertData().get

  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage() {
      title = "Market"
      scene = SceneManager.switchTo(Scenes.Start)
    }
  }
}
