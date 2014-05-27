package dellstore.dao

import com.googlecode.mapperdao._
import dellstore.model.Order

/**
 * maps to this table:
 *
 * CREATE TABLE orders
 * (
 * orderid serial NOT NULL,
 * orderdate date NOT NULL,
 * customerid integer,
 * netamount numeric(12,2) NOT NULL,
 * tax numeric(12,2) NOT NULL,
 * totalamount numeric(12,2) NOT NULL,
 * CONSTRAINT orders_pkey PRIMARY KEY (orderid ),
 * CONSTRAINT fk_customerid FOREIGN KEY (customerid)
 * REFERENCES customers (customerid) MATCH SIMPLE
 * ON UPDATE NO ACTION ON DELETE SET NULL
 * )
 */
object OrderEntity extends Entity[Int, SurrogateIntId, Order]("orders")
{
	val orderid = key("orderid") autogenerated (_.id)
	val date = column("orderdate") to (_.date)
	/**
	 * map CustomerEntity to orders.customerid . We also want to lazy load this entity
	 * and to do so we need to declare the getter Order.customer
	 */
	val customer = manytoone(CustomerEntity) foreignkey "customerid" getter "customer" to (_.customer)
	val netAmount = column("netamount") to (_.netAmount)
	val tax = column("tax") to (_.tax)
	val totalAmount = column("totalamount") to (_.totalAmount)
	/**
	 * An order can have many order lines. orderlines.orderid is the foreign key for this relationship and
	 * we do the mapping here.
	 *
	 * We also want to lazy load this entity
	 * and to do so we need to declare the getter Order.orderLines
	 */
	val orderLines = onetomany(OrderLineEntity) foreignkey "orderid" getter "orderLines" to (_.orderLines)

	def constructor(implicit m: ValuesMap) = new Order(date, customer, netAmount, tax, totalAmount, orderLines) with Stored
	{
		val id: Int = orderid
	}
}
