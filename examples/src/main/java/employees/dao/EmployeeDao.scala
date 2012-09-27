package employees.dao
import com.googlecode.mapperdao._
import com.googlecode.mapperdao.utils._
import employees.model._

/**
 * @author kostantinos.kougios
 *
 * 2 Sep 2011
 */
class EmployeeDao(val mapperDao: MapperDao, val queryDao: QueryDao) extends NaturalStringIdCRUD[Employee] {
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
