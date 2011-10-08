package com.rits.routers
import ru.circumflex._, core._, web._, freemarker._

/**
 * @author kostantinos.kougios
 *
 * 9 Oct 2011
 */
class CatalogRouter extends RequestRouter("/catalogue") {
	get("/list") = ftl("catalogue/list.ftl")
}