package cqrses

import java.util.UUID

import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestKit, ImplicitSender}
import cqrses.Basket
import org.scalatest.{WordSpecLike, Matchers, BeforeAndAfterAll}

class BasketTest(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {
  def this() = this(ActorSystem("MySpec"))
  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }

  "The Basket actor" must {

    "toggle items" in {
      val id = UUID.randomUUID()
      val basket = system.actorOf(Props(new Basket(id)))
      system.eventStream.subscribe(self, classOf[Basket.Added])
      system.eventStream.subscribe(self, classOf[Basket.Removed])

      basket ! Basket.Toggle("eggs")
      expectMsg(Basket.Added("eggs"))

      basket ! Basket.Toggle("eggs")
      expectMsg(Basket.Removed("eggs"))

      basket ! Basket.Toggle("eggs")
      expectMsg(Basket.Added("eggs"))

      basket ! Basket.Toggle("bacon")
      expectMsg(Basket.Added("bacon"))

    }
    "recover after restart" in {
      val id = UUID.randomUUID()
      val basket = system.actorOf(Props(new Basket(id)))
      system.eventStream.subscribe(self, classOf[Basket.Added])
      system.eventStream.subscribe(self, classOf[Basket.Removed])

      basket ! Basket.Toggle("eggs")
      expectMsg(Basket.Added("eggs"))
      system.stop(basket)

      val basket2 = system.actorOf(Props(new Basket(id)))

      basket2 ! Basket.Toggle("eggs")
      expectMsg(Basket.Removed("eggs"))

    }
  }
}
