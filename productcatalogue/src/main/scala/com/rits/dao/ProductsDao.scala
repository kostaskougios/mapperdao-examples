package com.rits.dao

import com.googlecode.mapperdao.utils._
import com.rits.model._

/**
 * The product dao, as simple as a dao can be! By mixing in the
 * TransactionalIntIdCRUD[Product], we get CRUD methods which occur
 * within a transaction and hence all relevant tables (product, category,
 * attribute,price) will either be updated in 1 transaction or rolled back
 * if there is a problem.
 *
 * By mixin the IntIdAll[Product] trait, the dao acquires methods that
 * return pages of rows from the product table.
 *
 * @author kostantinos.kougios
 *
 * 10 Oct 2011
 */
abstract class ProductsDao extends TransactionalSurrogateIntIdCRUD[Product] with SurrogateIntIdAll[Product] {
	val entity = ProductEntity
}
