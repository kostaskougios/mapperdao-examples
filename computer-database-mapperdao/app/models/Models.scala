package models
import java.util.Date

//import org.scala_tools.time.Imports._

case class Company(name: String)
case class Computer(name: String, introduced: Option[Date], discontinued: Option[Date], company: Option[Company])

/**
 * Helper for pagination.
 */
case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
	lazy val prev = Option(page - 1).filter(_ >= 0)
	lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}
