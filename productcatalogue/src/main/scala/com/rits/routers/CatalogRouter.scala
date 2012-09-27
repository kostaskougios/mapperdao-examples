package com.rits.routers

import ru.circumflex._
import core._
import web._
import freemarker._
import com.rits.dao.Daos
import com.rits.model._
import routers._
import com.googlecode.mapperdao.utils._
import com.googlecode.mapperdao._

/**
 * @author kostantinos.kougios
 *
 * 9 Oct 2011
 */
class CatalogRouter extends RequestRouter("/catalogue") {
	import Daos._

	/**
	 * controller for the list of products
	 */
	get("/list") = {
		val pageNum = param("page")
		val page = if (pageNum.isEmpty) 1 else pageNum.toInt
		// fetch this page of products from the database
		val products = productsDao.page(page, Preferences.RowsPerPage)
		// fetch also the count of product pages
		val numOfPages = productsDao.countPages(Preferences.RowsPerPage)

		// render those
		ftl("catalogue/list.ftl", Map("products" -> products, "numOfPages" -> numOfPages))
	}

	/**
	 * edit product form, "get" method
	 */
	get("/edit/:id") = {
		val product = productById(param("id").toInt)
		ftl("catalogue/edit.ftl", Map("product" -> product, "saved" -> flash.getOrElse("saved", false)))
	}

	/**
	 * edit product form, "post" method
	 */
	post("/edit/:id") = {
		// for simplicity we'll skip validation

		// get the old product from the database
		val oldProduct = productById(param("id").toInt)

		/**
		 * processing the form submit is split into 3 parts:
		 *
		 * 1.	from the posted form, re-create prices, attributes, categories
		 * 2.	calculate the modified collections for prices, attributes, categories
		 * 3.	using mapperdao and the dao layer, update the product
		 *
		 *  We'll start with step 1:
		 */
		val groups = getGroups
		// get the modified prices for this product
		val newPrices = groups.group("price").filterNot(t => t._2("currency").isEmpty).map { t =>
			val (row, m) = t
			new Price(m("currency"), m("unitPrice").toDouble, m("salePrice").toDouble)
		}.toSet

		// now get the modified attributes
		val newAttrs = groups.group("attribute").filterNot(t => t._2("name").isEmpty).map { t =>
			val (row, m) = t
			// there is 1 and only 1 row for attributes with the same name and value.
			// hence we need to get from or create the attribute into the db 
			attributesDao.getOrCreate(m("name"), m("value"))
		}.toSet

		// get the modified categories
		val newCategories = groups.group("category").filterNot(t => t._2("hierarchy").isEmpty).map { t =>
			val (row, m) = t
			val hierarchy = m("hierarchy").split(",").toList
			categoriesDao.createHierarchy(hierarchy)
		}

		/**
		 * now we need to proceed to step 2:
		 * 		calculate the modified collections.
		 */

		// import mapperdao's collection manipulation helpers.
		// those will help us to get the updated collections
		import Helpers._
		// merges the 2 sets, returning modifiedPrices==newPrices
		// but modifiedPrices retains instances from oldProduct.prices
		val modifiedPrices = merge(oldProduct.prices, newPrices)
		// same for attributes
		val modifiedAttributes = merge(oldProduct.attributes, newAttrs)
		// ... and categories
		val modifiedCategories = merge(oldProduct.categories, newCategories)

		// tags, for simple entities we don't have to merge
		// the sets, we can just create a new set
		val tags = param("tags").split(",").map(_.trim).toSet
		/**
		 * now we are ready to create the updated product and store it into
		 * the database using mapperdao and the dao layer:
		 */

		// now we can create the new instance of the Product
		val newProduct = Product(
			param("title"),
			param("description"),
			modifiedPrices,
			modifiedAttributes,
			modifiedCategories,
			tags
		)
		val updated = oldProduct match {
			case p: Product with SurrogateIntId => productsDao.update(p, newProduct)
			case _ => productsDao.create(newProduct)
		}
		flash("saved") = true
		redirect("../edit/" + updated.id)
	}

	/**
	 * utility method to get a product from the database or create an empty one
	 */
	private def productById(id: Int) = if (id == 0) Product.empty else productsDao.retrieve(id).get
}