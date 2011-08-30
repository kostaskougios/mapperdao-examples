package dellstore

import java.util.Properties
import org.apache.commons.dbcp.BasicDataSourceFactory
import com.rits.orm.utils.Setup
import dellstore.dao.CategoryDao
import dellstore.dao.ProductDao

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

	val (jdbc, mapperDao, queryDao) = Setup.postGreSql(dataSource, List(CategoryDao.CategoryEntity, ProductDao.ProductEntity))

	val categoryDao = new CategoryDao(mapperDao, queryDao)
	val productDao = new ProductDao(mapperDao, queryDao)

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
	}

}