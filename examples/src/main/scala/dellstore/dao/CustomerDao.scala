package dellstore.dao

import com.googlecode.mapperdao._
import com.googlecode.mapperdao.utils._
import dellstore.model._

/**
 * @author kostantinos.kougios
 *
 *         1 Sep 2011
 */
abstract class CustomerDao extends TransactionalSurrogateIntIdCRUD[Customer] with SurrogateIntIdAll[Customer]
{

	import queryDao._

	val entity = CustomerEntity

	import Query._

	private val c = CustomerEntity

	/**
	 * all customers of a state. List[Customer with IntId]
	 */
	def byState(state: String) =
		query(select from c where c.state === state)

	/**
	 * all customers with this lastName. List[Customer with IntId]
	 */
	def byLastName(lastName: String) =
		query(select from c where c.lastname === lastName)
}
