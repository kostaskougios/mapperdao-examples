package dellstore.model

/**
 * @author kostantinos.kougios
 *
 *         30 Aug 2011
 */
class Inventory(
	val product: Product,
	val stock: Int,
	val sales: Int)
{

	override def equals(o: Any) = o match {
		case i: Inventory => i.product == product
		case _ => false
	}

	override def hashCode = product.hashCode

	override def toString = "Inventory(%d,%d)".format(stock, sales)
}
