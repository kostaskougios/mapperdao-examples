package dellstore.dao

import com.googlecode.mapperdao._
import dellstore.model.OrderLine

/**
 * maps to this table:
 *
 * CREATE TABLE orderlines
 * (
 * orderlineid integer NOT NULL,
 * orderid integer NOT NULL,
 * prod_id integer NOT NULL,
 * quantity smallint NOT NULL,
 * orderdate date NOT NULL,
 * CONSTRAINT fk_orderid FOREIGN KEY (orderid)
 * REFERENCES orders (orderid) MATCH SIMPLE
 * ON UPDATE NO ACTION ON DELETE CASCADE
 * )
 */
object OrderLineEntity extends Entity[Int, NaturalIntId, OrderLine]("orderlines")
{
	val orderlineid = key("orderlineid") to (_.id)
	val order = manytoone(OrderEntity) foreignkey "orderid" to (_.order)
	val product = onetoone(ProductEntity) foreignkey "prod_id" to (_.product)
	val quantity = column("quantity") to (_.quantity)
	val orderdate = column("orderdate") to (_.date)

	def constructor(implicit m: ValuesMap) = new OrderLine(orderlineid, order, product, quantity, orderdate) with Stored
}
