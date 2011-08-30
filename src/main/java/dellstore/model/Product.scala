package dellstore.model

/**
 * @author kostantinos.kougios
 *
 * 30 Aug 2011
 */
class Product(val category: Category, val title: String) {
	override def equals(o: Any) = o match {
		case p: Product => p.category == category && p.title == title
		case _ => false
	}

	override def hashCode = title.hashCode

	override def toString = "Product(%s,%s)".format(category, title)
}