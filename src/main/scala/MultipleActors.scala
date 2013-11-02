package example
import actors._

case object Ping
case object Pong

class Producer(consumer: ActorRef) extends Actor {
  val name = "producer"
  def response: Response = {
    case Ping => consumer ! Ping
  }
}

class Consumer extends Actor {
  val name = "consumer"
  def response: Response = {
    case Ping => println("Got a ping message!")
  }
}
