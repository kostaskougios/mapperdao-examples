package dellstore.dao

import org.apache.commons.dbcp.BasicDataSourceFactory
import java.util.Properties
import com.googlecode.mapperdao.utils.Setup

/**
 * @author kostantinos.kougios
 *
 *         11 Aug 2012
 */
object Daos
{
	println("Connecting to database")

	val properties = new Properties
	properties.load(getClass.getResourceAsStream("/dellstore.properties"))
	val dataSource = BasicDataSourceFactory.createDataSource(properties)
	println("Configuring mapperdao")

	val (j, m, q, txM) = Setup.postGreSql(dataSource,
		List(CategoryEntity, ProductEntity, InventoryEntity, CustomerEntity, OrderEntity, OrderLineEntity)
	)

	val categoryDao = new CategoryDao
	{
		val (mapperDao, queryDao, txManager) = (m, q, txM)
	}
	val productDao = new ProductDao
	{
		val mapperDao = m
		val queryDao = q
		val txManager = txM
	}
	val customerDao = new CustomerDao
	{
		val mapperDao = m
		val queryDao = q
		val txManager = txM
	}
	val orderDao = new OrderDao
	{
		val mapperDao = m
		val queryDao = q
		val txManager = txM
	}

	println("Initialization done.")

}