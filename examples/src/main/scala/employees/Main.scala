package employees

import org.apache.commons.dbcp.BasicDataSourceFactory
import java.util.Properties
import com.googlecode.mapperdao.utils.Setup
import employees.model.Employee
import employees.dao._

/**
 * @author kostantinos.kougios
 *
 *         2 Sep 2011
 */
object Main extends App
{

	println("Connecting to database")

	val properties = new Properties
	properties.load(getClass.getResourceAsStream("/employees.properties"))
	val dataSource = BasicDataSourceFactory.createDataSource(properties)

	println("Configuring mapperdao")

	val (jdbc, mapperDao, queryDao, txManager) = Setup.mysql(dataSource,
		List(EmployeeEntity, EmployeeDepartmentEntity, DepartmentEntity)
	)
	val employeeDao = new EmployeeDao(mapperDao, queryDao)
	println("Initialization done")

	val cmd = args(0)

	println("executing " + cmd)

	cmd match {
		case "by-lastName" =>
			val employees = employeeDao.maleByLastName(args(1))
			printEmployees(employees)
	}

	def printEmployees(employees: List[Employee]) {
		employees.foreach {
			e =>
				println(e)
		}
	}
}