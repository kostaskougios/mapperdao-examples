package dellstore.dao

import com.googlecode.mapperdao._
import dellstore.model.OrderLine

object OrderLineEntity extends SimpleEntity[OrderLine]("orderlines") {
	val orderlineid = key("orderlineid") to (_.id)
	val order = manytoone(OrderEntity) foreignkey "orderid" to (_.order)
	val product = onetoone(ProductEntity) foreignkey "prod_id" to (_.product)
	val quantity = column("quantity") to (_.quantity)
	val orderdate = column("orderdate") to (_.date)

	def constructor(implicit m) = new OrderLine(orderlineid, order, product, quantity, orderdate) with Persisted
}
