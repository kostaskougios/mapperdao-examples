package com.rits.mysecrets

import java.util.Properties
import org.apache.commons.dbcp.BasicDataSourceFactory
import com.googlecode.mapperdao.utils.Setup
import com.rits.mysecrets.dao._
import com.googlecode.mapperdao.jdbc.Transaction
import net.liftweb.common.Logger

/**
 * initializes the database connection and creates the dao singletons
 *
 * @author kostantinos.kougios
 *
 * 15 Sep 2011
 */
object Daos extends Logger {

	val database = System.getProperty("db")
	if (database == null) throw new IllegalStateException("-Ddb must be provided, i.e. -Ddb=mysql")

	// database connectivity setup (private to this factory)
	// We'll use apache's basic data source to pool the connections
	private val properties = new Properties
	properties.load(getClass.getResourceAsStream("/jdbc.%s.properties".format(database)))

	info("initializing database pool and dao's, connecting to %s at %s".format(database, properties.get("url")))

	private val dataSource = BasicDataSourceFactory.createDataSource(properties)

	// and we'll connect to the database, registering UserEntiry,SecretEntity...
	private val entities = List(
		UserDao.UserEntity,
		SecretDao.SecretEntity,
		ReminderDao.ReminderEntity
	)

	private val (j, md, q) = database match {
		case "postgresql" => Setup.postGreSql(dataSource, entities)
		case "mysql" => Setup.mysql(dataSource, entities)
	}
	// our dao's are transactional, hence we need a transaction manager. MapperDao uses spring's
	// excellent support for transactions via the org.springframework.transaction.PlatformTransactionManager
	// (Our application is not using spring framework to manage beans, this app is a typical lift web app)
	private val txM = Transaction.transactionManager(j)

	// dao components (singletons), we also inject the dependencies
	val userDao = new UserDao {
		val (mapperDao, queryDao, txManager) = (md, q, txM)
	}

	val secretDao = new SecretDao {
		val (mapperDao, queryDao, txManager) = (md, q, txM)
	}

	val reminderDao = new ReminderDao {
		val (mapperDao, queryDao, txManager) = (md, q, txM)
	}

	info("dao initialization complete")

}