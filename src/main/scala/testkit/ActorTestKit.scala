package actors.testkit
import actors._

trait ActorTestKit {
  def createTestkitActor(actor: Actor)(implicit sys: ActorSystem): TestKitActorRef =
    new TestKitActorRef(sys.createActor(actor))

  class TestKitActorRef(private[this] val actor: ActorRef) {
    private val respondedToMessages = scala.collection.mutable.Stack[Any]()

    def !(msg: Any): Unit = {
      respondedToMessages.push(msg)
      actor ! msg
    }

    def respondedTo[T](messages: T*): Boolean =
      messages.toList.forall(respondedToMessages.contains(_))
  }
}
