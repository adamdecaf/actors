package actors

case object RestartActor
case object StopActor

object Actor {
  type Response = PartialFunction[Any, Unit]
}

trait Actor extends ChildrenManagement {
  protected type Response = Actor.Response

  def name: String
  def response: Response

  def beforeStart(): Unit = {}
  def beforeStop(): Unit = {}
}

abstract class ActorRef private[actors] (protected val actor: Actor) extends Dispatcher {
  protected val mailbox = Queue
  actor.beforeStart()
  tick()

  def !(msg: Any): Unit = msg match {
    case StopActor =>
      actor.beforeStop()
      shutdownDispatcher()

    case RestartActor =>
      actor.beforeStop()
      mailbox.dequeueAll(_ => true)
      actor.beforeStart()

    case _ => mailbox.enqueue(msg)
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
