package dellstore.model

/**
 * the Customer entity is stored in 1 flat table in the database, but we can model it better by
 * creating embedded entities (CreditCard,Address) and an Enumeration for the Gender
 *
 * @author kostantinos.kougios
 *
 * 1 Sep 2011
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

case class CreditCard(val cardType: Int, val card: String, val expiration: String)
case class Address(val address1: String, val address2: String, val city: String, val state: String,
	val zip: Int, val country: String, val region: Int)

import Gender._
class Customer(val firstName: String, val lastName: String, val address: Address, val email: String, val phone: String, val creditCard: CreditCard,
		val userName: String, val password: String, val age: Int, val income: Int, val gender: Gender) {

	override def toString = "Customer(%s,%s,%s,%s,%s)".format(firstName, lastName, address, creditCard, gender)
}

