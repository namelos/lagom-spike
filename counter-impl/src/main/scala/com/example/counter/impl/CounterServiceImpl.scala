package com.example.counter.impl

import com.example.counter.api.CounterService
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.Future

class CounterServiceImpl extends CounterService {
  def counter(id: String) = ServiceCall { _ =>
    Future.successful(0)
  }

  def add(id: String) = ServiceCall { n =>
    Future.successful(n)
  }
}
