package com.rits.mysecrets.snippet

import scala.xml.NodeSeq
import net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http._
import SHtml._
import com.rits.mysecrets.model.User
import com.rits.mysecrets.Daos._
import S._

/**
 * takes care of new user registration
 *
 * @author kostantinos.kougios
 */
class Register {
	def form(in: NodeSeq): NodeSeq = {
		object email extends RequestVar("")
		object name extends RequestVar("")
		object pwd extends RequestVar("")
		object pwdVerify extends RequestVar("")

		def process() {
			if (Validation.isValid(
				(email.isEmpty(), "Email must be provided"),
				(!email.contains("@"), "Invalid Email address"),
				(name.isEmpty(), "Full Name must be provided"),
				(pwd.isEmpty(), "Password must be provided"),
				(pwd.get != pwdVerify.get, "Passwords don't match"),
				// make sure the database doesn't contain the same email address
				(userDao.byEmail(email.get).isDefined, "A user with the same email already exists")
			)) {
				// create the POSO (Plain Old Scala Object!)
				val u = new User(name.get, email.get, pwd.get)
				// persist it
				userDao.create(u)
				redirectTo("registration-succesfull")
			}
		}
		bind(
			"register", in,
			"email" -> text(email, email(_)),
			"name" -> text(name, name(_)),
			"pwd" -> password(pwd, pwd(_)),
			"pwdVerify" -> password(pwdVerify, pwdVerify(_)),
			"submit" -> submit("Register", process)
		)
	}
}