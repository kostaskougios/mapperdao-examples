package com.rits.mysecrets.snippet

import scala.xml._
import net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http._
import SHtml._
import S._

/**
 * @author kostantinos.kougios
 *
 * 17 Sep 2011
 */
class Global {
	def welcome(in: NodeSeq): NodeSeq = {
		UserVar.get.map(u => bind("user", in, "name" -> u.name)).getOrElse(Text(""))
	}
}