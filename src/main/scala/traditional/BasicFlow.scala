package traditional

import akka.actor.ActorRef
import akka.persistence.{AtLeastOnceDelivery, PersistentActor}
import messages._

class BasicFlow extends PersistentActor with AtLeastOnceDelivery {

  def emailActor: ActorRef = ???
  def fulfillmentActor: ActorRef = ???
  def financeActor: ActorRef = ???

  var state = State()
  var originalSender: ActorRef = ActorRef.noSender

  override def persistenceId: String = "asdsad"

  def receiveRecover: Receive = updateState

  override def receiveCommand: Receive = {
    case StartCmd ⇒
      originalSender = sender()
      emailActor ! EmailModule.SendMail
    case EmailModule.Done ⇒
      persist(MailSent) { evt ⇒
        originalSender ! Ack
        updateState(evt)
      }
    case FulfillmentModule.Fulfilled ⇒
      persist(Fulfilled) { evt ⇒
        updateState(evt)
      }
  }

  def updateState: Receive = {
    case MailSent ⇒
      state = state.copy(mailed = true)
      deliver(fulfillmentActor.path, { id ⇒ FulfillmentModule.FulfillOrder(id) })
    case Fulfilled(id, amount) ⇒
      confirmDelivery(id)
      state = state.copy(fulfilled = amount)
      deliver(financeActor.path, { id ⇒ FinanceModule.UpdateFinance })
  }

}
