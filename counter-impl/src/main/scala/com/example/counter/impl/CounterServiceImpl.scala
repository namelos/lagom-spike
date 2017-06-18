package com.example.counter.impl

import com.example.counter.api.CounterService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

class CounterServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends CounterService {
  def counter(id: String) = ServiceCall { _ =>
    val ref = persistentEntityRegistry.refFor[CounterEntity](id)
    ref.ask(GetState)
  }

  def add(id: String) = ServiceCall { n =>
    val ref = persistentEntityRegistry.refFor[CounterEntity](id)
    ref.ask(Add(n))
  }
}
