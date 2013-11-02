package actors

trait Logging {
  protected def log: Logger

  protected abstract class Logger {
    def warn(msg: String): Unit
  }
}

trait StoutLogging extends Logging {
  protected lazy val log = new Logger {
    def warn(msg: String) = println(msg)
  }
}
