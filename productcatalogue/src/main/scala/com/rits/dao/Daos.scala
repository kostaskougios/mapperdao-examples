package com.rits.dao
import java.util.Properties
import com.googlecode.mapperdao.utils.Setup
import com.googlecode.mapperdao.jdbc.Transaction
import org.apache.commons.dbcp.BasicDataSourceFactory

/**
 * @author kostantinos.kougios
 *
 * 10 Oct 2011
 */
object Daos {
	// database connectivity setup (private to this factory)
	// We'll use apache's basic data source to pool the connections
	private val properties = new Properties
	properties.load(getClass.getResourceAsStream("/jdbc.postgresql.properties"))
	private val dataSource = BasicDataSourceFactory.createDataSource(properties)

	// and we'll connect to postgresql database, registering UserEntiry,SecretEntity...
	import Entities._
	private val (j, md, q) = Setup.postGreSql(dataSource, List(ProductEntity, CategoryEntity, AttributeEntity, PriceEntity))
	// our dao's are transactional, hence we need a transaction manager. MapperDao uses spring's
	// excellent support for transactions via the org.springframework.transaction.PlatformTransactionManager
	// (Our application is not using spring framework to manage beans, this app is a typical lift web app)
	private val txM = Transaction.transactionManager(j)

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
}