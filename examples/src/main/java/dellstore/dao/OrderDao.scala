package dellstore.dao

import com.googlecode.mapperdao.utils._
import com.googlecode.mapperdao._
import dellstore.model._

/**
 * @author kostantinos.kougios
 *
 * 1 Sep 2011
 */
abstract class OrderDao extends TransactionalIntIdCRUD[Order] with IntIdAll[Order] {
	val entity = OrderEntity

	import Query._
	import queryDao._

	private val o = OrderEntity
	private val c = CustomerEntity

	/**
	 * returns all orders from customers living at the provided state
	 */
	def byState(state: String): List[Order with IntId] =
		query(select from o join (o, o.customer, c) where c.state === state)
	/**
	 * all orders that the totalamount is between min and max
	 */
	def byTotal(min: Double, max: Double): List[Order with IntId] =
		query(select from o where o.totalAmount >= min and o.totalAmount <= max)

	def of(customer: Customer) = query(
		select
			from o
			where o.customer === customer
	)
}
