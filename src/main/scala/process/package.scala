import processframework._

package object process {
  sealed trait Event extends Process.Event
  case class Fulfilled(id: Long, amount: Int) extends Event
  case object MailSent extends Event
  case object Started extends Event
}
