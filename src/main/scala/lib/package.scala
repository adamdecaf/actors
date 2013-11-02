package object actors {
  implicit val ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  type ExecutionContext = scala.concurrent.ExecutionContext

  type ActorsSet = scala.collection.mutable.Set[ActorRef]

  val Queue = scala.collection.mutable.Queue.empty[Any]
  type Queue[A] = scala.collection.mutable.Queue[A]
}
