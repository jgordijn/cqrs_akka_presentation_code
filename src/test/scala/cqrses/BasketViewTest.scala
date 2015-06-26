package cqrses

import java.util.UUID

import akka.actor.{Actor, ActorSystem, Props}
import akka.persistence.{RecoveryCompleted, PersistentActor}
import akka.testkit.{ImplicitSender, TestKit}
import cqrses.{BasketView, Basket}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

class BasketViewTest(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
  def this() = this(ActorSystem("MySpec"))
  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }

  "The BasketView actor" must {

    "return the complete list of items" in {
      val id = UUID.randomUUID()
      preparedata(id)
      val basketView = system.actorOf(Props(new BasketView(id)))
      basketView ! BasketView.GetAll
      expectMsg(List("eggs", "bacon", "bread", "milk").reverse)
    }
    "return the statistics" in {
      val id = UUID.randomUUID()
      preparedata(id)
      val basketView = system.actorOf(Props(new BasketView(id)))
      basketView ! BasketView.GetStats
      expectMsg(BasketView.Stat(6, 2))
    }
  }

  def preparedata(id: UUID) = {
    system.actorOf(Props(new PersistentActor {
      def persistenceId: String = s"order-$id"
      def receiveRecover: Receive = Actor.emptyBehavior
      def receiveCommand: Receive = {
        case "init" =>
          persist(Basket.Added("eggs")){_=>}
          persist(Basket.Added("ham")){_=>}
          persist(Basket.Removed("ham")){_=>}
          persist(Basket.Added("bacon")){_=>}
          persist(Basket.Added("bread")){_=>}
          persist(Basket.Added("Foo")){_=>}
          persist(Basket.Removed("Foo")){_=>}
          persist(Basket.Added("milk")){_=> sender() ! "done"}
      }
    })) ! "init"
    expectMsg("done")

  }
}
