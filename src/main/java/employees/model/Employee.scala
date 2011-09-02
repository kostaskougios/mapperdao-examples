package employees.model
import org.joda.time.DateTime

/**
 * @author kostantinos.kougios
 *
 * 2 Sep 2011
 */
object Gender extends Enumeration {
	type Gender = Value
	val Male, Female = Value

	def toString(gender: Gender) = gender match {
		case Male => "M"
		case Female => "F"
	}

	def fromString(g: String): Gender = g match {
		case "M" => Male
		case "F" => Female
	}
}

import Gender._

class Employee(val id: Int, val birthDate: DateTime, val firstName: String, val lastName: String, val gender: Gender, val hireDate: DateTime, val employeeDepartment: List[EmployeeDepartment]) {

	// not exactly the perfect equals method but does the trick for persisted entities
	override def equals(o: Any) = o match {
		case e: Employee => e.id == id
		case _ => false
	}

	override def toString = "Employee(%d,%s,%s,%s)".format(id, firstName, lastName, gender)
}
