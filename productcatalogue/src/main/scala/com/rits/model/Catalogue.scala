package com.rits.model

/**
 * the product catalogue domain model
 *
 * @author kostantinos.kougios
 *
 * 10 Oct 2011
 */
case class Product(
	val title: String,
	val description: String,
	val prices: Set[Price],
	val attributes: Set[Attribute],
	val categories: List[Category],
	val tags: Set[String])

case class Price(val currency: String, val unitPrice: Double, val salePrice: Double)

case class Attribute(val name: String, val value: String)

case class Category(val name: String, val parent: Option[Category]) {
	def commaSeparated: String = parent match {
		case Some(p) => "%s,%s".format(p.commaSeparated, name)
		case None => name
	}
}
