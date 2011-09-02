package employees.dao
import com.rits.orm.SimpleEntity
import employees.model.Department
import employees.model.Gender
import com.rits.orm.Persisted
import com.rits.orm.ValuesMap
import com.rits.orm.utils.SimpleCRUD
import com.rits.orm.MapperDao
import com.rits.orm.QueryDao
import com.rits.orm.Query
import com.rits.orm.utils.SimpleAll

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