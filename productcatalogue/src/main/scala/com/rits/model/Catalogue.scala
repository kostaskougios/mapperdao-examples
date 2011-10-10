package com.rits.model

/**
 * the product catalogue domain model
 *
 * @author kostantinos.kougios
 *
 * 10 Oct 2011
 */
class Product(
	val title: String,
	val description: String,
	val prices: Set[Price],
	val attributes: Set[Attribute],
	val categories: List[Category],
	val tags: Set[String])

class Price(val currency: String, val unitPrice: Double, val salePrice: Double)

class Attribute(val name: String, val value: String)

class Category(val name: String, val parent: Option[Category])
