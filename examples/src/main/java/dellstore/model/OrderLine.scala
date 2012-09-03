package dellstore.model
import org.joda.time.DateTime

/**
 * @author kostantinos.kougios
 *
 * 1 Sep 2011
 */
class OrderLine(
		val id: Int,
		val order: Order,
		val product: Product,
		val quantity: Int,
		val date: DateTime) {

	override def toString = "OrderLine(%d,%s,%d)".format(id, product.title, quantity)
}