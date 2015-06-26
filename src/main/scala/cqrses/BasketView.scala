package cqrses

import java.util.UUID

import akka.persistence.PersistentView

object BasketView {
  case object GetAll
  case object GetStats

  case class Stat(nrOfAdditions: Int, nrOfRemovals: Int)
}
class BasketView(id: UUID) extends PersistentView {
  import BasketView._
  def viewId: String = s"$persistenceId-view"
  def persistenceId: String = s"order-$id"

  var nrOfAdditions = 0
  var nrOfRemovals = 0
  var items = List.empty[String]

  def receive: Receive = {
    case Basket.Added(item) ⇒
      nrOfAdditions += 1
      items ::= item
    case Basket.Removed(item) ⇒
      nrOfRemovals += 1
      items = items.filterNot(_ == item)
    case GetAll               ⇒ sender() ! items
    case GetStats             ⇒ sender() ! Stat(nrOfAdditions, nrOfRemovals)
  }
}
