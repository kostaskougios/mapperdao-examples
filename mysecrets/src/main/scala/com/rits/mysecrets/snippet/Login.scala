package com.rits.mysecrets.snippet

import scala.xml.NodeSeq
import net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http._
import SHtml._
import S._
import com.rits.mysecrets.Daos._
import com.rits.mysecrets.model.User

/**
 * login form
 *
 * @author kostantinos.kougios
 */
class Login {

	def form(in: NodeSeq): NodeSeq = {
		object email extends RequestVar("")
		object pwd extends RequestVar("")

			def process() {
				val user = userDao.login(email.get, pwd.get)

				if (Validation.isValid(
					(!user.isDefined, "Invalid user name or password."),
					(email.isEmpty, "Please enter an email address"),
					(!email.contains("@"), "Invalid Email address"),
					(pwd.isEmpty(), "Password must be provided")
				)) {
					// store user in session
					UserVar.set(user)
					redirectTo("secrets/list")
				}
			}
		bind(
			"login", in,
			"email" -> text(email, email(_)),
			"pwd" -> password(pwd, pwd(_)),
			"submit" -> submit("Login", process)
		)
	}
}

object UserVar extends SessionVar[Option[User]](None)