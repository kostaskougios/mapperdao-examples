package employees
import org.apache.commons.dbcp.BasicDataSourceFactory
import java.util.Properties
import com.rits.orm.utils.Setup
import dellstore.dao.CategoryDao
import employees.dao.EmployeeDao
import employees.model.Employee

/**
 * @author kostantinos.kougios
 *
 * 2 Sep 2011
 */
object Main extends App {

	println("Connecting to database")

	val properties = new Properties
	properties.load(getClass.getResourceAsStream("/employees.properties"))
	val dataSource = BasicDataSourceFactory.createDataSource(properties)

	println("Configuring mapperdao")

	val (jdbc, mapperDao, queryDao) = Setup.mysql(dataSource,
		List(EmployeeDao.EmployeeEntity)
	)
	val employeeDao = new EmployeeDao(mapperDao, queryDao)
	println("Initialization done")

	val cmd = args(0)

	println("executing " + cmd)

	cmd match {
		case "by-lastName" =>
			val employees = employeeDao.byLastName(args(1))
			printEmployees(employees)
	}

	def printEmployees(employees: List[Employee]) {
		employees.foreach { e =>
			println(e)
		}
	}
}