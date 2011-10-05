package com.rits.mysecrets.snippet

import scala.xml.NodeSeq
import net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http._
import SHtml._
import S._
import net.liftweb.common.Logger
import com.rits.mysecrets.model.Secret

/**
 * this serves the purpose of doing quick tests
 *
 * @author kostantinos.kougios
 *
 * 5 Oct 2011
 */
class Test extends Logger {
	def form(in: NodeSeq): NodeSeq = {
		object nameVar extends RequestVar("")
		object phoneVar extends RequestVar("")
		object secretVar extends RequestVar[Secret](null)
		info("Rendering: name: %s phone: %s, secret: %s".format(nameVar.get, phoneVar.get, secretVar.get))
		val secret = new Secret("xxx", "yyy", null, Set(), Set())
		secretVar.set(secret)

		bind("test", in,
			"name" -> text(nameVar, nameVar(_)),
			"phone" -> text(phoneVar, phoneVar(_)),
			"secret" -> hidden(() => secretVar(secret)),
			"add" -> submit("Add", () => {
				info("Adding: name: %s phone:%s".format(nameVar.get, phoneVar.get))
			}),
			"submit" -> submit("Save", () => {
				info("Submit: name: %s phone: %s, secret: %s".format(nameVar.get, phoneVar.get, secretVar.get))
				redirectTo("index")
			})
		)
	}
}