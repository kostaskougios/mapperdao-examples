package com.rits.dao

import com.googlecode.mapperdao.utils.TransactionalIntIdCRUD
import com.rits.model._
import Entities._
import com.googlecode.mapperdao.utils.IntIdAll

/**
 * @author kostantinos.kougios
 *
 * 10 Oct 2011
 */
abstract class ProductsDao extends TransactionalIntIdCRUD[Product] with IntIdAll[Product] {
	val entity = ProductEntity
}
