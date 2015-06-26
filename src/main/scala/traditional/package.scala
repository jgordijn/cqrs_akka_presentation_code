package object traditional {
  sealed trait Event
  case class Fulfilled(id: Long, amount: Int) extends Event
  case object MailSent extends Event
}
