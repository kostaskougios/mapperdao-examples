package dellstore.dao

import com.googlecode.mapperdao._
import dellstore.model._

object InventoryEntity extends SimpleEntity[Inventory]("inventory") {
	val stock = column("quan_in_stock") to (_.stock)
	val sales = column("sales") to (_.sales)
	val product = onetoone(ProductEntity) foreignkey "prod_id" to (_.product)
	def constructor(implicit m: ValuesMap) = new Inventory(product, stock, sales) with Persisted
}
