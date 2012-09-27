package dellstore.dao
import com.googlecode.mapperdao._
import dellstore.model._
import com.googlecode.mapperdao.utils._

/**
 * @author kostantinos.kougios
 *
 * 30 Aug 2011
 */
abstract class ProductDao extends TransactionalSurrogateIntIdCRUD[Product] with SurrogateIntIdAll[Product] {
	val entity = ProductEntity

	import Query._
	import queryDao._

	private val p = ProductEntity

	def idRange(min: Int, max: Int) = query(select from p where p.id >= min and p.id <= max)
}
