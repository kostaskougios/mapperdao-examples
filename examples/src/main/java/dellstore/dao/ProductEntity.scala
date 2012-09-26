package dellstore.dao

import com.googlecode.mapperdao._
import dellstore.model._

object ProductEntity extends Entity[IntId, Product]("products") {
	val id = key("prod_id") autogenerated (_.id)
	val title = column("title") to (_.title)
	val actor = column("actor") to (_.actor)
	val price = column("price") to (_.price)
	val category = manytoone(CategoryEntity) foreignkey "category" to (_.category)
	val special = column("special") to (v => v.special)
	val inventory = onetoonereverse(InventoryEntity) foreignkey "prod_id" to (_.inventory)

	def constructor(implicit m) = new Product(category, title, actor, price, special, inventory)  with IntId {
		val id: Int = ProductEntity.id
	}
}
