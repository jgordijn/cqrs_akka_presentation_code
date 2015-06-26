package process

import akka.actor.{ActorContext, ActorRef}
import processframework.ProcessStep
import messages._

class StartFulfillmentStep(fulfillmentActor: ActorRef)(implicit val context: ActorContext) extends ProcessStep[State] {

  def execute()(implicit process: ActorRef): Execution = { _ ⇒ fulfillmentActor ! FulfillmentModule.FulfillOrder }
  def receiveCommand: CommandToEvent = { case FulfillmentModule.Fulfilled ⇒ Fulfilled(0, 100) }
  def updateState: UpdateFunction = { case Fulfilled(id, amount) ⇒ { state ⇒ state.copy(fulfilled = amount) } }

}
