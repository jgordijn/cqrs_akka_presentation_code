package object messages {
  case object StartCmd
  case object Ack


  object EmailModule {
    case object SendMail
    case object Done
  }

  object FulfillmentModule {
    case class FulfillOrder(id: Long)
    case class Fulfilled(id: Long)
  }

  object FinanceModule {
    object UpdateFinance
  }

  case class State(mailed: Boolean = false, fulfilled: Int = 0)

}
