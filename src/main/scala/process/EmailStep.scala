package process

import akka.actor.{ ActorContext, ActorRef }
import processframework.ProcessStep
import messages._

class EmailStep(emailActor: ActorRef)(implicit val context: ActorContext) extends ProcessStep[State] {

  def execute()(implicit process: ActorRef): Execution = { _ ⇒ emailActor ! EmailModule.SendMail }
  def receiveCommand: CommandToEvent = {
    case EmailModule.Done ⇒
      MailSent
  }
  def updateState: UpdateFunction = {
    case MailSent ⇒ { state ⇒
      markDone() // This marks the step as completed
      state.copy(mailed = true)
    }
  }

}
