package com.rits.dao
import java.util.Properties
import com.googlecode.mapperdao.utils.Setup
import com.googlecode.mapperdao.jdbc.Transaction
import org.apache.commons.dbcp.BasicDataSourceFactory
import com.googlecode.mapperdao.utils.Database

/**
 * this is a factory for all the dao's. It initializes the database connection pool
 * and mapperdao. It then injects all dependencies to the dao's.
 *
 * @author kostantinos.kougios
 *
 * 10 Oct 2011
 */
object Daos {
	// database connectivity setup (private to this factory)
	// We'll use apache's basic data source to pool the connections
	private val properties = new Properties
	val database = System.getProperty("database")
	private val validDatabases = Set("postgresql", "mysql", "sqlserver", "oracle")
	if (!validDatabases(database)) {
		println("-Ddatabase value is not correct")
		throw new IllegalArgumentException("Please configure -Ddatabase=X, X can be postgresql, mysql, sqlserver or oracle")
	}
	properties.load(getClass.getResourceAsStream("/jdbc.%s.properties".format(database)))
	private val dataSource = BasicDataSourceFactory.createDataSource(properties)

	// and we'll connect to the database, registering UserEntiry,SecretEntity...
	private val entities = List(ProductEntity, CategoryEntity, AttributeEntity, PriceEntity)
	private val (j, md, q, txM) = Setup(Database.byName(database), dataSource, entities, None)

	// and now the dao singletons
	val productsDao = new ProductsDao {
		val (mapperDao, queryDao, txManager) = (md, q, txM)
	}

	val attributesDao = new AttributesDao {
		val (mapperDao, queryDao, txManager) = (md, q, txM)
	}

	val categoriesDao = new CategoriesDao {
		val (mapperDao, queryDao, txManager) = (md, q, txM)
	}

	val jdbc = j
}