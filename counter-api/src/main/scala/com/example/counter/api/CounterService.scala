package com.example.counter.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait CounterService extends Service {
  import Service._

  def counter(id: String): ServiceCall[NotUsed, Int]

  def add(id: String): ServiceCall[Int, Done]

  def descriptor = {
    named("counter")
      .withCalls(
        pathCall("/api/counter/:id", counter _),
        pathCall("/api/counter/:id", add _)
      )
      .withAutoAcl(true)
  }
}
