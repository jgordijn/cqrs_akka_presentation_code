package process

import akka.actor.{ActorContext, ActorRef}
import messages._
import processframework.ProcessStep

class InitStep()(implicit val context: ActorContext) extends ProcessStep[State] {

  def execute()(implicit process: ActorRef): Execution = { _ => }
  def receiveCommand: CommandToEvent = { case StartCmd ⇒
    markDone()
    Started
  }
  def updateState: UpdateFunction = { case MailSent ⇒ { state ⇒ state.copy(mailed = true) } }

}
