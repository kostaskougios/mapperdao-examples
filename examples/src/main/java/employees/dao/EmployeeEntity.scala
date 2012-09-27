package employees.dao

import com.googlecode.mapperdao._
import employees.model._

/**
 * mapping for table:
 *
 * CREATE TABLE `employees` (
 * `emp_no` int(11) NOT NULL,
 * `birth_date` date NOT NULL,
 * `first_name` varchar(14) NOT NULL,
 * `last_name` varchar(16) NOT NULL,
 * `gender` enum('M','F') NOT NULL,
 * `hire_date` date NOT NULL,
 * PRIMARY KEY (`emp_no`)
 * )
 */
object EmployeeEntity extends Entity[NaturalStringId, Employee]("employees", classOf[Employee]) {
	val emp_no = key("emp_no") to (_.id)
	val birth_date = column("birth_date") to (_.birthDate)
	val first_name = column("first_name") to (_.firstName)
	val last_name = column("last_name") to (_.lastName)
	val gender = column("gender") to (employee => Gender.toString(employee.gender))
	val hire_date = column("hire_date") to (_.hireDate)
	val employeeDepartment = onetomany(EmployeeDepartmentEntity) foreignkey "emp_no" to (_.employeeDepartment)

	def constructor(implicit m) = {
		val g = Gender.fromString(gender)
		new Employee(emp_no, birth_date, first_name, last_name, g, hire_date, employeeDepartment) with NaturalStringId
	}
}