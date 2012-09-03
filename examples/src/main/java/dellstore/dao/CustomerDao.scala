package dellstore.dao

import com.googlecode.mapperdao._
import com.googlecode.mapperdao.utils._
import dellstore.model._

/**
 * @author kostantinos.kougios
 *
 * 1 Sep 2011
 */
abstract class CustomerDao extends TransactionalIntIdCRUD[Customer] with IntIdAll[Customer] {
	import queryDao._
	val entity = CustomerEntity
	import Query._

	private val c = CustomerEntity

	def byState(state: String): List[Customer with IntId] = query(select from c where c.state === state)
	def byLastName(lastName: String): List[Customer with IntId] = query(select from c where c.lastname === lastName)
}
