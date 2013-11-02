package actors

object Actor {
  type Response = PartialFunction[Any, Unit]
}

trait Actor extends ChildrenManagement {
  protected type Response = Actor.Response

  def name: String
  def response: Response
}

abstract class ActorRef private[actors] (protected val actor: Actor) extends Dispatcher {
  protected val mailbox = Queue
  tick()

  def !(msg: Any): Unit = {
    mailbox.enqueue(msg)
  }
}

object ActorSystem {
  def apply(name: String) = new ActorSystem(name)
}

class ActorSystem(name: String) extends ChildrenManagement {
  def createActor(act: Actor): ActorRef = createChild(act)
}

trait ChildrenManagement {
  private[this] val children: ActorsSet = scala.collection.mutable.Set.empty

  protected def createChild(child: Actor) = {
    val act = new ActorRef(child) with TimeBasedDispatcher
    children += act
    act
  }
}
