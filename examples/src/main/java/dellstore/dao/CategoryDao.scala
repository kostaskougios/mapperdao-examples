package dellstore.dao

import dellstore.model._
import com.googlecode.mapperdao.utils._
import com.googlecode.mapperdao.SurrogateIntId

/**
 * dao for the Category class. Mixin methods from TransactionalCRUD (create,retrieve,update,
 * delete) and All (all and page methods)
 *
 * @author kostantinos.kougios
 *
 * 30 Aug 2011
 */
abstract class CategoryDao
		extends TransactionalCRUD[Int, SurrogateIntId, Category]
		with All[Int, SurrogateIntId, Category] {
	val entity = CategoryEntity
}
