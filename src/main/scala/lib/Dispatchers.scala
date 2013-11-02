package actors

trait Dispatcher {
  protected def actor: Actor
  protected def mailbox: Queue[Any]
  protected def tick()(implicit exc: ExecutionContext): Unit
}

trait TimeBasedDispatcher extends Dispatcher with StoutLogging {

  protected final def tick()(implicit exc: ExecutionContext): Unit = {
    exc.execute(new java.lang.Runnable {
      def run() = {
        Thread.sleep(1000)
        if (mailbox.nonEmpty) {
          val msg = mailbox.dequeue()
          if (actor.response.lift(msg).isEmpty) {
            log.warn(s"Ignoring unhandled message ${msg}")
          }
        }
        tick()
      }
    })
  }
}
