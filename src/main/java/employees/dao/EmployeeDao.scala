package employees.dao
import com.rits.orm.SimpleEntity
import employees.model.Employee
import employees.model.Gender
import com.rits.orm.Persisted
import com.rits.orm.ValuesMap
import com.rits.orm.utils.CRUD
import com.rits.orm.MapperDao
import com.rits.orm.QueryDao
import com.rits.orm.Query

/**
 * @author kostantinos.kougios
 *
 * 2 Sep 2011
 */
class EmployeeDao(val mapperDao: MapperDao, val queryDao: QueryDao) extends CRUD[AnyRef, Employee, Int] {
	import EmployeeDao._
	import queryDao._
	import Query._
	val entity = EmployeeEntity

	// alias for queries
	val e = EmployeeEntity

	def byLastName(lastName: String) = query(select from e where e.last_name === lastName)
}

object EmployeeDao {
	object EmployeeEntity extends SimpleEntity[Employee]("employees", classOf[Employee]) {
		val emp_no = pk("emp_no", _.id)
		val birth_date = datetime("birth_date", _.birthDate)
		val first_name = string("first_name", _.firstName)
		val last_name = string("last_name", _.lastName)
		val gender = string("gender", employee => Gender.toString(employee.gender))
		val hire_date = datetime("hire_date", _.hireDate)

		val constructor = (m: ValuesMap) => {
			val g = Gender.fromString(m(gender))
			new Employee(m(emp_no), m(birth_date), m(first_name), m(last_name), g, m(hire_date)) with Persisted {
				val valuesMap = m
			}
		}
	}

}