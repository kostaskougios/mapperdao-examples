package com.rits.routers

import ru.circumflex._
import core._
import web._
import freemarker._
import com.rits.dao.Daos
import com.rits.model._
import routers._

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
		ftl("catalogue/edit.ftl", Map("product" -> product))
	}

	post("/edit/:id") = {
		val id = param("id").toInt
		val newPrices = getGroups.group("price") { (row, m) =>
			new Price(m("currency"), m("unitPrice").toDouble, m("salePrice").toDouble)
		}
		println("\n\n" + newPrices + "\n\n")
		val oldProduct = productsDao.retrieve(id).get
		val newProduct = Product(
			param("title"),
			param("description"),
			oldProduct.prices,
			oldProduct.attributes,
			oldProduct.categories,
			oldProduct.tags
		)
		productsDao.update(oldProduct, newProduct)
		redirect("../edit/" + id)
		//redirect("../list")
	}
}