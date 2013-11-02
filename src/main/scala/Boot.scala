package example
import actors._

object Boot {
  def main(args: Array[String]) = {
    implicit lazy val sys = new ActorSystem("actor-system")

    val actor1 = sys.createActor(new Actor {
      lazy val name = "actor1"
      lazy val response: Response = {
        case s: String => println("String: " + s)
        case _ =>
      }
    })

    actor1 ! "this should print to stout"
    actor1 ! 12

    val consumer = sys.createActor(new Consumer)
    val producer = sys.createActor(new Producer(consumer))

    producer ! Ping

    while(true) {}
  }
}
