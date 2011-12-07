package dao
import com.googlecode.mapperdao.utils.Setup
import com.googlecode.mapperdao.jdbc.Transaction

/**
 * @author kostantinos.kougios
 *
 * 7 Dec 2011
 */
object Daos {
	val dataSource = play.db.DB.getDataSource("default")

	val (jdbc, mapperDao, queryDao) = Setup.h2(dataSource, List(ComputerEntity))
	val txManager = Transaction.transactionManager(jdbc)

	val computerDao = new ComputerDao {
	}

	val companyDao = new CompanyDao {
	}
}