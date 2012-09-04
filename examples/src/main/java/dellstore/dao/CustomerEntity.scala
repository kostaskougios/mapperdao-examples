package dellstore.dao

import com.googlecode.mapperdao._
import dellstore.model._

/**
 * maps to this table:
 *
 * CREATE TABLE customers
 * (
 * customerid serial NOT NULL,
 * firstname character varying(50) NOT NULL,
 * lastname character varying(50) NOT NULL,
 * address1 character varying(50) NOT NULL,
 * address2 character varying(50),
 * city character varying(50) NOT NULL,
 * state character varying(50),
 * zip integer,
 * country character varying(50) NOT NULL,
 * region smallint NOT NULL,
 * email character varying(50),
 * phone character varying(50),
 * creditcardtype integer NOT NULL,
 * creditcard character varying(50) NOT NULL,
 * creditcardexpiration character varying(50) NOT NULL,
 * username character varying(50) NOT NULL,
 * password character varying(50) NOT NULL,
 * age smallint,
 * income integer,
 * gender character varying(1),
 * CONSTRAINT customers_pkey PRIMARY KEY (customerid )
 * )
 */
object CustomerEntity extends Entity[IntId, Customer]("customers") {
	val customerId = key("customerid") autogenerated (_.id)

	val firstname = column("firstname") to (_.firstName)
	val lastname = column("lastname") to (_.lastName)
	// address is an embedded class, but it easy to configure.
	val address1 = column("address1") to (_.address.address1)
	val address2 = column("address2") to (_.address.address2)
	val city = column("city") to (_.address.city)
	val state = column("state") to (_.address.state)
	val zip = column("zip") to (_.address.zip)
	val country = column("country") to (_.address.country)
	val region = column("region") to (_.address.region)
	// end of address config
	val email = column("email") to (_.email)
	val phone = column("phone") to (_.phone)
	// embedded CreditCard class
	val creditcardtype = column("creditcardtype") to (_.creditCard.cardType)
	val creditcard = column("creditcard") to (_.creditCard.card)
	val creditcardexpiration = column("creditcardexpiration") to (_.creditCard.expiration)
	// end of CreditCard config
	val username = column("username") to (_.userName)
	val password = column("password") to (_.password)
	val age = column("age") to (_.age)
	val income = column("income") to (_.income)
	// the Gender is stored as String in the db but it is modeled as an Enumeration 
	val gender = column("gender") to (customer => Gender.toString(customer.gender))

	// constructor
	def constructor(implicit m) =
		{
			// instantiate the embedded entities
			val address = Address(address1, address2, city, state, zip, country, region)
			val creditCard = CreditCard(creditcardtype, creditcard, creditcardexpiration)
			val g = Gender.fromString(gender)
			new Customer(firstname, lastname, address, email, phone, creditCard, username, password, age, income, g) with Persisted with IntId {
				val id: Int = customerId
			}
		}
}
