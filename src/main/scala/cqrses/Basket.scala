package cqrses

import java.util.UUID

import akka.persistence.PersistentActor

object Basket {
  sealed trait Command
  case class Toggle(item: String) extends Command

  sealed trait Event
  case class Added(item: String) extends Event
  case class Removed(item: String) extends Event
}

class Basket(id: UUID) extends PersistentActor {
  import Basket._
  var items = Set.empty[String]

  def persistenceId: String = s"order-$id"
  def receiveCommand: Receive = {
    case Toggle(item) ⇒
      if (!items.contains(item)) // First some logic
        persist(Added(item)) { evt ⇒ // Then persist
          context.system.eventStream.publish(evt)
          updateState(evt) // Then update the state
        }
      else
        persist(Removed(item)) { evt ⇒
          context.system.eventStream.publish(evt)
          updateState(evt)
        }
  }
  def receiveRecover: Receive = {
    case evt: Event ⇒ updateState(evt)
  }
  def updateState: PartialFunction[Any, Unit] = {
    case Added(item)   ⇒ items += item
    case Removed(item) ⇒ items = items.filterNot(_ == item)
  }
}
