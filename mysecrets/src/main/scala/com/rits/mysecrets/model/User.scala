package com.rits.mysecrets.model

/**
 * a user of the system.
 *
 * @author kostantinos.kougios
 *
 * 16 Sep 2011
 */
class User(val name: String, val email: String, val password: String) {

	/**
	 * hashCode & equals are overriden not because they are needed
	 * by mapperdao but becase this class is stored into collections
	 */
	override def hashCode = email.hashCode
	override def equals(o: Any) = o match {
		case u: User => name == u.name && email == u.email
		case _ => false
	}
	override def toString = "User(%s)".format(email)
}
