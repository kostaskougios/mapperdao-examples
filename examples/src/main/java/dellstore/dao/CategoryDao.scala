package dellstore.dao

import dellstore.model._
import com.googlecode.mapperdao.utils.TransactionalCRUD
import com.googlecode.mapperdao.IntId
import com.googlecode.mapperdao.utils.All

/**
 * dao for the Category class. Mixin methods from TransactionalCRUD (create,retrieve,update,
 * delete) and All (all and page methods)
 *
 * @author kostantinos.kougios
 *
 * 30 Aug 2011
 */
abstract class CategoryDao
		extends TransactionalCRUD[Int, IntId, Category]
		with All[IntId, Category] {
	val entity = CategoryEntity
}
