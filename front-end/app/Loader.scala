import com.example.counter.api.CounterService
import com.lightbend.lagom.scaladsl.api.{ServiceAcl, ServiceInfo}
import com.lightbend.lagom.scaladsl.client.LagomServiceClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, Mode}
import play.api.ApplicationLoader.Context
import play.api.i18n.I18nComponents
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._
import controllers.HomeController
import router.Routes

import scala.collection.immutable
import scala.concurrent.ExecutionContext

abstract class WebGateway(context: Context) extends BuiltInComponentsFromContext(context)
  with I18nComponents with AhcWSComponents with LagomServiceClientComponents {
  override lazy val serviceInfo = ServiceInfo(
    "front-end",
    Map("front-end" -> immutable.Seq(ServiceAcl.forPathRegex("(?!/api/).*")))
  )
  override implicit lazy val executionContext: ExecutionContext = actorSystem.dispatcher
  override lazy val router = {
    val prefix = "/"
    wire[Routes]
  }

  lazy val counterService = serviceClient.implement[CounterService]

  lazy val home = wire[HomeController]
}

class WebGatewayLoader extends ApplicationLoader {
  override def load(context: Context) = context.environment.mode match {
    case Mode.Dev => (new WebGateway(context) with LagomDevModeComponents).application
    case _ => throw new Exception("Not ready for deployment yet.")
  }
}
