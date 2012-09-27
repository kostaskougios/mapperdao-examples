package com.rits.dao

import com.googlecode.mapperdao.utils._
import com.rits.model._
import com.googlecode.mapperdao._

/**
 * @author kostantinos.kougios
 *
 * 10 Oct 2011
 */
abstract class AttributesDao extends TransactionalSurrogateIntIdCRUD[Attribute] {
	protected val entity = AttributeEntity

	protected val queryDao: QueryDao
	import Query._
	import queryDao._

	// alias for queries
	private val a = entity

	/**
	 * either retrieves the attribute from the database, or creates it.
	 */
	def getOrCreate(name: String, value: String): Attribute = {
		val q = (
			select
			from a
			where
			a.name === name
			and a.value === value
		)
		querySingleResult(q).getOrElse(create(Attribute(name, value)))
	}
}