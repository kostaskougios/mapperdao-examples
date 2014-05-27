package dellstore.dao

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, FunSuite}

/**
 * @author kostantinos.kougios
 *
 *         11 Aug 2012
 */
@RunWith(classOf[JUnitRunner])
class OrdersDaoSuite extends FunSuite with Matchers
{

	import Daos._

	test("orders for a specific customer") {
		val customer = customerDao.byLastName("FRQTPCUFCF")(0)
		val orders = orderDao.of(customer)
		orders.size should be > 0
	}

	test("orders for a category") {
		val orders = orderDao.byCategory("Games")
		orders.size should be > 0
	}
}

