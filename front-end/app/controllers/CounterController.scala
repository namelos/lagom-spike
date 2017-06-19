package controllers

import com.example.counter.api.CounterService
import play.api.mvc._

import scala.concurrent.ExecutionContext

class CounterController(counterService: CounterService)(implicit ec: ExecutionContext) extends Controller {
  def show = Action.async {
    counterService.counter("1").invoke().map { value =>
      Ok(views.html.counter.render(value))
    }
  }
}

