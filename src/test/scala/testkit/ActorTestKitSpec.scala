package actors.testkit
import actors._
import org.specs2.specification.Scope
import org.specs2.mutable.Specification

object ActorTestKitSpec extends Specification {

  "The TestKit" should {
    "know when events have been sent" in new context {
      actor ! Ping
      actor ! "hello"

      actor.respondedTo(Ping) must beTrue
      actor.respondedTo("hello") must beTrue
      actor.respondedTo(12) must beFalse
    }
  }

  trait context extends Scope with ActorTestKit {
    case object Ping

    implicit val sys = ActorSystem("yo")
    val actor = createTestkitActor(new Actor {
      val name = "test-actor"
      def response: Response = {
        case Ping => println("Got ping message")
      }
    })
  }
}
