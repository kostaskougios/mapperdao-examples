package employees.dao
import com.googlecode.mapperdao.SimpleEntity
import employees.model.Employee
import employees.model.Gender
import com.googlecode.mapperdao.Persisted
import com.googlecode.mapperdao.ValuesMap
import com.googlecode.mapperdao.utils.SimpleCRUD
import com.googlecode.mapperdao.MapperDao
import com.googlecode.mapperdao.QueryDao
import com.googlecode.mapperdao.Query
import employees.model.EmployeeDepartment
import employees.model.Department

/**
 * @author kostantinos.kougios
 *
 * 2 Sep 2011
 */
class EmployeeDao(val mapperDao: MapperDao, val queryDao: QueryDao) extends SimpleCRUD[Employee, Int] {
	import EmployeeDao._
	import queryDao._
	import Query._
	val entity = EmployeeEntity

	// alias for queries
	val e = EmployeeEntity

	def maleByLastName(lastName: String) = query(
		select
			from e
			where e.last_name === lastName
			and e.gender === "M"
	)
}

object EmployeeDao {
	object EmployeeEntity extends SimpleEntity[Employee]("employees", classOf[Employee]) {
		val emp_no = key("emp_no") to (_.id)
		val birth_date = column("birth_date") to (_.birthDate)
		val first_name = column("first_name") to (_.firstName)
		val last_name = column("last_name") to (_.lastName)
		val gender = column("gender") to (employee => Gender.toString(employee.gender))
		val hire_date = column("hire_date") to (_.hireDate)
		val employeeDepartment = onetomany(EmployeeDepartmentEntity) foreignkey "emp_no" to (_.employeeDepartment)

		def constructor(implicit m: ValuesMap) = {
			val g = Gender.fromString(gender)
			new Employee(emp_no, birth_date, first_name, last_name, g, hire_date, employeeDepartment) with Persisted
		}
	}

	object EmployeeDepartmentEntity extends SimpleEntity[EmployeeDepartment]("dept_emp", classOf[EmployeeDepartment]) {
		val emp_no = key("emp_no") to (ed => if (ed.employee != null) ed.employee.id else -1)
		val dept_no = key("dept_no") to (ed => if (ed.department != null) ed.department.no else null)
		val from_date = column("from_date") to (_.fromDate)
		val to_date = column("to_date") to (_.toDate)

		val employee = manytoone(EmployeeEntity) foreignkey "emp_no" to (_.employee)
		val department = manytoone(DepartmentDao.DepartmentEntity) foreignkey "dept_no" to (_.department)

		def constructor(implicit m: ValuesMap) = new EmployeeDepartment(employee, department, from_date, to_date) with Persisted
	}
}