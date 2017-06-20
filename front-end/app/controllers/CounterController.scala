package controllers

import com.example.counter.api.CounterService
import play.api.data._
import play.api.data.Forms._
import play.api.i18n._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class CounterController(counterService: CounterService)(implicit ec: ExecutionContext, implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {
  val addForm = Form(
    mapping(
      "n" -> number
    )(Add.apply)(Add.unapply)
  )

  def show(id: String) = Action.async {
    counterService.counter(id).invoke().map { value =>
      Ok(views.html.counter(value, addForm))
    }
  }

  def add(id: String) = Action.async { implicit request =>
    addForm.bindFromRequest.fold(
      _ => Future.successful(BadRequest("Error")),
      add => counterService.add(id).invoke(add.n).map { _ =>
        Redirect(s"/counter/$id")
      }
    )
  }
}

case class Add(n: Int)
