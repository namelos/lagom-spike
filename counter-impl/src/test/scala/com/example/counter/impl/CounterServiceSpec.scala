package com.example.counter.impl

import com.example.counter.api.CounterService
import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}

class CounterServiceSpec extends AsyncWordSpec with Matchers with BeforeAndAfterAll {
  private val server = ServiceTest.startServer(
    ServiceTest.defaultSetup
  ) { ctx =>
    new CounterApplication(ctx) with LocalServiceLocator
  }

  val client = server.serviceClient.implement[CounterService]

  def AfterAll() = server.stop()

  "Counter service" should {
    "return value" in {
      client.counter("1").invoke().map { value =>
        value shouldBe 0
      }
    }

    "return same value" in {
      client.add("1").invoke(2).map { value =>
        value shouldBe 2
      }
    }
  }
}
