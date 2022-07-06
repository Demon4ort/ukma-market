package market

import market.utils.Alerts.areYouSure
import market.utils.FutureFxOps.defaultTimeout
import scalafx.application.Platform
import scalafx.scene.control.{ButtonType, ContextMenu, MenuItem, TableView}
import scalafx.scene.input.KeyCode.E

import scala.concurrent.duration.{Duration, DurationInt, FiniteDuration}
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.reflect.ClassTag

package object utils {


  final implicit class FutureFxOps[T](val f: Future[T]) extends AnyVal {
    def futureValue(timeout: Duration): T = Await.result(f, timeout)

    def futureValue: T = Await.result(f, defaultTimeout)
  }

  object FutureFxOps {
    val defaultTimeout: FiniteDuration = 5 seconds
  }


}