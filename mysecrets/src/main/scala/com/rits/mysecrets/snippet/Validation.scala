package com.rits.mysecrets.snippet

import net.liftweb.http.S._
/**
 * validation helper methods
 *
 * @author kostantinos.kougios
 *
 * 17 Sep 2011
 */
object Validation {
	def isValid(conditions: (Boolean, String)*): Boolean = conditions.reverse.foldRight(true) { (t, sofar) =>
		val e = t._1
		if (e) error(t._2)
		sofar && (!e)
	}

	def onValidation(success: => Unit, conditions: List[(Boolean, String)]) = if (Validation.isValid(conditions: _*)) {
		success
	}

}