package employees.dao
import com.googlecode.mapperdao._
import employees.model._

/**
 * mapping for table:
 *
 * CREATE TABLE `dept_emp` (
 * `emp_no` int(11) NOT NULL,
 * `dept_no` char(4) NOT NULL,
 * `from_date` date NOT NULL,
 * `to_date` date NOT NULL,
 * PRIMARY KEY (`emp_no`,`dept_no`),
 * KEY `emp_no` (`emp_no`),
 * KEY `dept_no` (`dept_no`),
 * CONSTRAINT `dept_emp_ibfk_1` FOREIGN KEY (`emp_no`) REFERENCES `employees` (`emp_no`) ON DELETE CASCADE,
 * CONSTRAINT `dept_emp_ibfk_2` FOREIGN KEY (`dept_no`) REFERENCES `departments` (`dept_no`) ON DELETE CASCADE
 * )
 *
 */
object EmployeeDepartmentEntity extends SimpleEntity[EmployeeDepartment]("dept_emp", classOf[EmployeeDepartment]) {
	val emp_no = key("emp_no") to (ed => if (ed.employee != null) ed.employee.id else -1)
	val dept_no = key("dept_no") to (ed => if (ed.department != null) ed.department.no else null)
	val from_date = column("from_date") to (_.fromDate)
	val to_date = column("to_date") to (_.toDate)

	val employee = manytoone(EmployeeEntity) foreignkey "emp_no" to (_.employee)
	val department = manytoone(DepartmentEntity) foreignkey "dept_no" to (_.department)

	def constructor(implicit m) = new EmployeeDepartment(employee, department, from_date, to_date) with Persisted
}
