package com.rits.model

/**
 * the product catalogue domain model.
 *
 * A product has a set of prices for each currency, a number of attributes and categories.
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
		val tags: Set[String]) {
	def tagsCommaSeparated = tags.mkString(",")
}

object Product {
	def empty = new Product("", "", Set(), Set(), List(), Set())
}
case class Price(val currency: String, val unitPrice: Double, val salePrice: Double)

case class Attribute(val name: String, val value: String)

case class Category(val name: String, val parent: Option[Category]) {
	/**
	 * comma separated list representation of the category hierarchy
	 */
	def commaSeparated: String = parent match {
		case Some(p) => "%s,%s".format(p.commaSeparated, name)
		case None => name
	}
}
