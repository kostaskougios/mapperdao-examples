package dellstore.model

/**
 * @author kostantinos.kougios
 *
 * 30 Aug 2011
 */
class Category(val name: String) {

	override def equals(o: Any) = o match {
		case c: Category => c.name == name
		case _ => false
	}

	override def hashCode = name.hashCode
}