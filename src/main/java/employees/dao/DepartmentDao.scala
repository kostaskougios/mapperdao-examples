package employees.dao
import com.googlecode.mapperdao.SimpleEntity
import employees.model.Department
import employees.model.Gender
import com.googlecode.mapperdao.Persisted
import com.googlecode.mapperdao.ValuesMap
import com.googlecode.mapperdao.utils.SimpleCRUD
import com.googlecode.mapperdao.MapperDao
import com.googlecode.mapperdao.QueryDao
import com.googlecode.mapperdao.Query
import com.googlecode.mapperdao.utils.SimpleAll

/**
 * @author kostantinos.kougios
 *
 * 2 Sep 2011
 */
class DepartmentDao(val mapperDao: MapperDao, val queryDao: QueryDao) extends SimpleCRUD[Department, Int] with SimpleAll[Department] {
	import DepartmentDao._
	import queryDao._
	import Query._

	val entity = DepartmentEntity
}

object DepartmentDao {
	object DepartmentEntity extends SimpleEntity[Department]("departments", classOf[Department]) {
		val dept_no = pk("dept_no", _.no)
		val dept_name = string("dept_name", _.name)

		val constructor = (m: ValuesMap) => new Department(m(dept_no), m(dept_name)) with Persisted {
			val valuesMap = m
		}
	}

}