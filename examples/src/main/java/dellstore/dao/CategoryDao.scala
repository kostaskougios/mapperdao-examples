package dellstore.dao

import dellstore.model._
import com.googlecode.mapperdao.utils.TransactionalCRUD
import com.googlecode.mapperdao.IntId
import com.googlecode.mapperdao.utils.All

/**
 * dao for the Category class
 *
 * @author kostantinos.kougios
 *
 * 30 Aug 2011
 */
abstract class CategoryDao extends TransactionalCRUD[IntId, Category, Int] with All[IntId, Category] {
	val entity = CategoryEntity
}
