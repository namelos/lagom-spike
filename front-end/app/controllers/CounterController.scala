package controllers

import com.example.counter.api.CounterService
import play.api.mvc._

import scala.concurrent.ExecutionContext

class CounterController(counterService: CounterService)(implicit ec: ExecutionContext) extends Controller {
  def show(id: String) = Action.async {
    counterService.counter(id).invoke().map { value =>
      Ok(views.html.counter.render(value))
    }
  }
}

