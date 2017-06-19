package controllers

import com.example.counter.api.CounterService
import play.api.mvc._

import scala.concurrent.ExecutionContext

class HomeController(counterService: CounterService)(implicit ec: ExecutionContext) extends Controller {
  def index = Action.async {
    counterService.counter("1").invoke().map { value =>
//      Ok(s"you value is ${value toString}")
      Ok(views.html.index.render(value))
    }
  }
}
