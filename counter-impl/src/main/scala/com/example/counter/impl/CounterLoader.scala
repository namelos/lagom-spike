package com.example.counter.impl

import com.example.counter.api.CounterService
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server.{LagomApplication, LagomApplicationContext, LagomApplicationLoader}
import play.api.libs.ws.ahc.AhcWSComponents
import com.softwaremill.macwire._

class CounterLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext): LagomApplication =
    new CounterApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new CounterApplication(context) with LagomDevModeComponents

  override def describeServices = List(
    readDescriptor[CounterService]
  )
}

abstract class CounterApplication(context: LagomApplicationContext) extends LagomApplication(context) with AhcWSComponents {
  lazy val lagomServer = serverFor[CounterService](wire[CounterServiceImpl])
}
