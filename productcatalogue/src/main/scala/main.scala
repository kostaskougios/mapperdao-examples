package com.rits.productcatalogue

import ru.circumflex._, core._, web._, freemarker._
import java.util.Date
import com.rits.routers._

class Main extends RequestRouter {
	val log = new Logger("com.rits.productcatalogue")

	'currentDate := new Date

	get("/test") = "I'm fine, thanks!"
	get("/") = ftl("index.ftl")

	any("/catalogue/*") = new CatalogRouter

}