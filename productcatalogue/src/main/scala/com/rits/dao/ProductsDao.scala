package com.rits.dao

import com.googlecode.mapperdao.utils.TransactionalIntIdCRUD
import com.rits.model._
import Entities._

/**
 * @author kostantinos.kougios
 *
 * 10 Oct 2011
 */
abstract class ProductsDao extends TransactionalIntIdCRUD[Product] {
	val entity = ProductEntity
}
