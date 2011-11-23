package com.rits.dao

import com.googlecode.mapperdao.utils.TransactionalIntIdCRUD
import com.rits.model._
import com.googlecode.mapperdao.QueryDao
import com.googlecode.mapperdao.Query

/**
 * @author kostantinos.kougios
 *
 * 10 Oct 2011
 */
abstract class CategoriesDao extends TransactionalIntIdCRUD[Category] {
	protected val entity = CategoryEntity
	protected val queryDao: QueryDao
	import queryDao._
	import Query._

	// alias to category entity
	private val c = entity

	/**
	 * gets the persisted database or creates it if it doesn't exist
	 */
	def getOrCreate(name: String, parent: Option[Category]): Category =
		{
			val q = (
				select
				from c
				where c.name === name and c.parent === parent.getOrElse(null)
			)
			querySingleResult(q).getOrElse(create(Category(name, parent)))
		}

	/**
	 * creates (if not already present) a hierarchy of categories and
	 * returns the leaf category.
	 */
	def createHierarchy(categories: List[String]): Category =
		{
			def createHierarchy(categories: List[String], parent: Option[Category]): Category =
				{
					val newParent = getOrCreate(categories.head, parent)
					if (categories.tail.isEmpty) newParent else createHierarchy(categories.tail, Some(newParent))
				}
			createHierarchy(categories, None)
		}
}