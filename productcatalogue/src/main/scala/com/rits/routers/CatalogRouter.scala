package com.rits.routers
import ru.circumflex._
import core._
import web._
import freemarker._
import com.rits.dao.Daos

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

	get("/edit") = {
		val id = param("id").toInt
		val product = productsDao.retrieve(id)
		ftl("catalogue/edit.ftl", Map("product" -> product))
	}
}