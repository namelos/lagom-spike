package com.example.counter.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Service, ServiceCall}

trait CounterService extends Service {
  import Service._

  def counter(id: String): ServiceCall[NotUsed, Int]

  def add(id: String): ServiceCall[Int, Int]

  def descriptor = {
    named("counter")
      .withCalls(
        pathCall("/api/counter/:id", counter _),
        pathCall("/api/counter/:id", add _)
      )
      .withAutoAcl(true)
  }
}
