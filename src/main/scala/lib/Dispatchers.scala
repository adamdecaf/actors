package actors

trait Dispatcher {
  protected def actor: Actor
  protected def mailbox: Queue[Any]
  protected def shutdownDispatcher(): Unit
  protected def tick()(implicit exc: ExecutionContext): Unit
}

trait TimeBasedDispatcher extends Dispatcher with StoutLogging {
  private[this] var running = true

  protected def shutdownDispatcher() = (running = false)

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
        if (running) tick() // Stack overflow..
      }
    })
  }
}
