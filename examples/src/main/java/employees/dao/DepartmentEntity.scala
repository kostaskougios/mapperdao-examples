package employees.dao

import com.googlecode.mapperdao._
import employees.model._

/**
 * mapping for table:
 *
 * CREATE TABLE `departments` (
 * `dept_no` char(4) NOT NULL,
 * `dept_name` varchar(40) NOT NULL,
 * PRIMARY KEY (`dept_no`),
 * UNIQUE KEY `dept_name` (`dept_name`)
 * )
 *
 */
object DepartmentEntity extends Entity[String, NaturalStringId, Department]("departments") {
	val dept_no = key("dept_no") to (_.no)
	val dept_name = column("dept_name") to (_.name)

	def constructor(implicit m) = new Department(dept_no, dept_name) with NaturalStringId
}
