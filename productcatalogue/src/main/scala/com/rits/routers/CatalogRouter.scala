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
		val products = productsDao.page(1, 10)
		ftl("catalogue/list.ftl", Map("products" -> products))
	}
}