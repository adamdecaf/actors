package actors
import scala.collection.mutable.Set

case class AddWorker(ref: ActorRef)
case class WorkerFinished(ref: Actor)
case object AllWorkersFinished

trait WorkerSupport { self: Actor =>
  private[this] val children: Set[Long] = Set.empty

  protected def workerManagement: Response = {
    case AddWorker(ref) =>
      children.add(ref.id)
    case WorkerFinished(act) =>
      children.remove(act.id)
      if (children.isEmpty) response.lift(AllWorkersFinished)
  }
}
