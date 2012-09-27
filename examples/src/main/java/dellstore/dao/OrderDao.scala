package dellstore.dao

import com.googlecode.mapperdao.utils._
import com.googlecode.mapperdao._
import dellstore.model._

/**
 * mixes in methods from TransactionalIntIdCRUD, IntIdAll and adds a couple
 * of extra query methods
 *
 * @author kostantinos.kougios
 *
 * 1 Sep 2011
 */
abstract class OrderDao extends TransactionalSurrogateIntIdCRUD[Order] with SurrogateIntIdAll[Order] {
	val entity = OrderEntity

	import Query._
	import queryDao._

	private val o = OrderEntity
	private val ol = OrderLineEntity
	private val pr = ProductEntity
	private val ca = CategoryEntity
	private val c = CustomerEntity

	/**
	 * returns all orders from customers living at the provided state. List[Order with IntId]
	 */
	def byState(state: String) =
		query(select from o join (o, o.customer, c) where c.state === state)

	/**
	 * all orders that the totalamount is between min and max. List[Order with IntId]
	 */
	def byTotal(min: Double, max: Double) =
		query(select from o where o.totalAmount >= min and o.totalAmount <= max)

	def of(customer: Customer) = query(
		select
			from o
			where o.customer === customer
	)

	def byCategory(categoryName: String) = query(
		QueryConfig.default.copy(lazyLoad = LazyLoad.all),
		select
			from o
			join (o, o.orderLines, ol)
			join (ol, ol.product, pr)
			join (pr, pr.category, ca)
			where ca.name === categoryName
	)
}
