package dellstore.dao
import com.googlecode.mapperdao.Entity
import com.googlecode.mapperdao.IntId
import dellstore.model._
import com.googlecode.mapperdao.ValuesMap
import com.googlecode.mapperdao.Persisted
import com.googlecode.mapperdao.utils.All
import com.googlecode.mapperdao.MapperDao
import com.googlecode.mapperdao.QueryDao
import com.googlecode.mapperdao.utils.IntIdCRUD
import com.googlecode.mapperdao.utils.IntIdAll
import com.googlecode.mapperdao.SimpleEntity
import com.googlecode.mapperdao.Query
import com.googlecode.mapperdao.utils.TransactionalIntIdCRUD

/**
 * @author kostantinos.kougios
 *
 * 30 Aug 2011
 */
abstract class ProductDao extends TransactionalIntIdCRUD[Product] with IntIdAll[Product] {
	import ProductDao._
	val entity = ProductEntity

	import Query._
	import queryDao._

	private val p = ProductEntity

	def idRange(min: Int, max: Int) = query(select from p where p.id >= min and p.id <= max)
}

object ProductDao {
	object InventoryEntity extends SimpleEntity[Inventory]("inventory", classOf[Inventory]) {
		val stock = int("quan_in_stock", _.stock)
		val sales = int("sales", _.sales)
		val product = oneToOne(classOf[Product], "prod_id", _.product)
		def constructor(implicit m: ValuesMap) = new Inventory(product, stock, sales) with Persisted
	}
	object ProductEntity extends Entity[IntId, Product]("products", classOf[Product]) {
		val id = intAutoGeneratedPK("prod_id", _.id)
		val title = string("title", _.title)
		val actor = string("actor", _.actor)
		val price = float("price", _.price)
		val category = manyToOne("category", classOf[Category], _.category)
		val special = boolean("special", v => v.special)
		val inventory = oneToOneReverse(classOf[Inventory], "prod_id", _.inventory)

		def constructor(implicit m: ValuesMap) = new Product(category, title, actor, price, special, inventory) with Persisted with IntId {
			val id: Int = ProductEntity.id
		}
	}
}