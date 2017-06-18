package com.example.counter.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json.Json

import scala.collection.immutable.Seq

class CounterEntity extends PersistentEntity {
  type Command = CounterCommand[_]
  type Event = CounterEvent
  type State = CounterState

  def initialState = CounterState(0)

  def behavior = {
    case CounterState(_) => Actions()
      .onCommand[Add, Done] { case (Add(x), ctx, _) => ctx.thenPersist(Added(x)) { _ => ctx.reply(Done) } }
      .onReadOnlyCommand[GetState.type, Int] { case (GetState, ctx, CounterState(n)) => ctx.reply(n) }
      .onEvent { case (Added(x), CounterState(n)) => CounterState(n + x) }
  }
}

case class CounterState(n: Int)
object CounterState { implicit val format = Json.format[CounterState] }

sealed trait CounterEvent extends AggregateEvent[CounterEvent] { def aggregateTag = CounterEvent.Tag }
object CounterEvent { val Tag = AggregateEventTag[CounterEvent] }
case class Added(n: Int) extends CounterEvent
object Added { implicit val format = Json.format[Added] }

sealed trait CounterCommand[R] extends ReplyType[R]
case class Add(n: Int) extends CounterCommand[Done]
object Add { implicit val format = Json.format[Add] }
case object GetState extends CounterCommand[Int] { implicit val format = JsonSerializer.emptySingletonFormat(GetState) }

object CounterSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[Add],
    JsonSerializer[Added],
    JsonSerializer[CounterState],
    JsonSerializer[GetState.type]
  )
}
