package dellstore

import java.util.Properties
import org.apache.commons.dbcp.BasicDataSourceFactory
import com.rits.orm.utils.Setup
import com.rits.orm.IntId
import dellstore.dao.CategoryDao
import dellstore.dao.CustomerDao
import dellstore.dao.OrderDao
import dellstore.dao.ProductDao
import dellstore.model.Customer
import dellstore.model.Order
import dellstore.model.Gender
import dellstore.model.CreditCard
import dellstore.model.Address
import java.util.UUID

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
		case "add-customer" =>
			val uuid = UUID.randomUUID()
			val customer = new Customer(args(1), args(2),
				Address("25 some street", "some avenue", "Athens", "GR", 85100, "Greece", 2), "email@x.x", "1234567",
				CreditCard(1, "1234 5678 9012 3456", "2011"), "userx" + uuid, "pwd", 22, 25000, Gender.Male)

			val inserted = customerDao.create(customer)

			println("added %d : %s".format(inserted.id, inserted))
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