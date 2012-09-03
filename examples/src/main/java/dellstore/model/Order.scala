package dellstore.model
import org.joda.time.DateTime

/**
 * @author kostantinos.kougios
 *
 * 1 Sep 2011
 */
class Order(
		val date: DateTime,
		val customer: Customer,
		val netAmount: BigDecimal,
		val tax: BigDecimal,
		val totalAmount: BigDecimal,
		val orderLines: List[OrderLine]) {

	override def toString = "Order(%s,%s,%s,orderlines: %s)".format(
		date,
		customer,
		totalAmount,
		orderLines)
}