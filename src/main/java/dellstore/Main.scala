package dellstore

import java.util.Properties
import org.apache.commons.dbcp.BasicDataSourceFactory
import com.rits.orm.utils.Setup
import dellstore.dao.CategoryDao
import dellstore.dao.ProductDao
import dellstore.dao.CustomerDao
import dellstore.model.Customer
import com.rits.orm.IntId
import dellstore.dao.OrderDao
import dellstore.model.Order

/**
 * dellstore sample for postgresql dellstore sample database
 *
 * @author kostantinos.kougios
 *
 * 30 Aug 2011
 */
object Main extends App {
	println("Connecting to database")

	val properties = new Properties
	properties.load(getClass.getResourceAsStream("/dellstore.properties"))
	val dataSource = BasicDataSourceFactory.createDataSource(properties)

	println("Configuring mapperdao")

	val (jdbc, mapperDao, queryDao) = Setup.postGreSql(dataSource,
		List(CategoryDao.CategoryEntity, ProductDao.ProductEntity, ProductDao.InventoryEntity, CustomerDao.CustomerEntity, OrderDao.OrderEntity, OrderDao.OrderLineEntity)
	)

	val categoryDao = new CategoryDao(mapperDao, queryDao)
	val productDao = new ProductDao(mapperDao, queryDao)
	val customerDao = new CustomerDao(mapperDao, queryDao)
	val orderDao = new OrderDao(mapperDao, queryDao)

	println("Initialization done.")

	val cmd = args(0)
	println("processing command " + cmd)

	cmd match {
		case "list-categories" =>
			val all = categoryDao.all

			println("id\tcategory")
			println("-----------------------------------------------")
			all.foreach { c =>
				println("%d :\t%s".format(c.id, c))
			}
		case "list-products" =>
			val all = productDao.all
			println("id\tproduct")
			println("-----------------------------------------------")
			all.foreach { p =>
				println("%d :\t%s".format(p.id, p))
			}
		case "list-customers" =>
			val all = customerDao.all
			println("customers")
			println("-----------------------------------------------")
			printCustomers(all)
		case "customers-by-state" =>
			val all = customerDao.byState(args(1))
			printCustomers(all)
		case "list-orders" =>
			println("id\torders")
			println("-----------------------------------------------")
			printOrders(orderDao.all)
		case "orders-by-state" =>
			println("id\torders")
			println("-----------------------------------------------")
			printOrders(orderDao.byState(args(1)))
		case "orders-by-total-price" =>
			println("id\torders")
			println("-----------------------------------------------")
			printOrders(orderDao.byTotal(args(1).toDouble, args(2).toDouble))
	}

	def printCustomers(customers: List[Customer with IntId]) {
		customers.foreach { printCustomer(_) }
	}
	def printCustomer(c: Customer with IntId) {
		println("%d :\t%s".format(c.id, c))
	}

	def printOrders(orders: List[Order with IntId]) {
		orders.foreach(o => println("%d :\t%s".format(o.id, o)))
	}
}