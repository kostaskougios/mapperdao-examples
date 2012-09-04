package employees.dao
import com.googlecode.mapperdao._
import com.googlecode.mapperdao.utils._
import employees.model._

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
