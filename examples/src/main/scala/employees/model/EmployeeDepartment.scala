package employees.model

import org.joda.time.DateTime

/**
 * @author kostantinos.kougios
 *
 *         2 Sep 2011
 */
class EmployeeDepartment(val employee: Employee, val department: Department, val fromDate: DateTime, val toDate: DateTime)
{
	override def toString = "EmployeeDepartment(%s %s,%s - %s)".format(employee.firstName, employee.lastName, department.no, department.name)
}