package employees.dao
import com.googlecode.mapperdao.SimpleEntity
import employees.model.Department
import employees.model.Gender
import com.googlecode.mapperdao.Persisted
import com.googlecode.mapperdao.ValuesMap
import com.googlecode.mapperdao.MapperDao
import com.googlecode.mapperdao.QueryDao
import com.googlecode.mapperdao.Query
import com.googlecode.mapperdao.utils.SimpleAll
import com.googlecode.mapperdao.utils.SimpleCRUD

/**
 * @author kostantinos.kougios
 *
 * 2 Sep 2011
 */
class DepartmentDao(val mapperDao: MapperDao, val queryDao: QueryDao) extends SimpleCRUD[Department, Int] with SimpleAll[Department] {
	import queryDao._
	import Query._

	val entity = DepartmentEntity
}

object DepartmentEntity extends SimpleEntity[Department]("departments", classOf[Department]) {
	val dept_no = key("dept_no") to (_.no)
	val dept_name = column("dept_name") to (_.name)

	def constructor(implicit m) = new Department(dept_no, dept_name) with Persisted
}
