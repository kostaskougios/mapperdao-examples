package com.rits.routers

import ru.circumflex._
import core._
import web._
import freemarker._
import com.rits.dao.Daos
import com.rits.model._
import routers._
import com.googlecode.mapperdao.utils.Helpers

/**
 * @author kostantinos.kougios
 *
 * 9 Oct 2011
 */
class CatalogRouter extends RequestRouter("/catalogue") {
	import Daos._
	get("/list") = {
		val pageNum = param("page")
		val page = if (pageNum.isEmpty) 1 else pageNum.toInt
		val products = productsDao.page(page, Preferences.RowsPerPage)
		val numOfPages = productsDao.countPages(Preferences.RowsPerPage)
		ftl("catalogue/list.ftl", Map("products" -> products, "numOfPages" -> numOfPages))
	}

	get("/edit/:id") = {
		val id = param("id").toInt
		val product = productsDao.retrieve(id)
		ftl("catalogue/edit.ftl", Map("product" -> product, "saved" -> flash.getOrElse("saved", false)))
	}

	post("/edit/:id") = {
		val id = param("id").toInt
		val newPrices = getGroups.group("price").filterNot(t => t._2("currency").isEmpty).map { t =>
			val (row, m) = t
			new Price(m("currency"), m("unitPrice").toDouble, m("salePrice").toDouble)
		}.toSet
		val oldProduct = productsDao.retrieve(id).get
		// import mapperdao's collection manipulation helpers
		import Helpers._
		val modifiedPrices = modified(oldProduct.prices, newPrices)
		val newProduct = Product(
			param("title"),
			param("description"),
			modifiedPrices,
			oldProduct.attributes,
			oldProduct.categories,
			oldProduct.tags
		)
		productsDao.update(oldProduct, newProduct)
		flash("saved") = true
		redirect("../edit/" + id)
		//redirect("../list")
	}
}