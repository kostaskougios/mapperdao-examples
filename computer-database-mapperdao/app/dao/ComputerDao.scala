package dao
import com.googlecode.mapperdao.utils.TransactionalIntIdCRUD
import models.Computer
import com.googlecode.mapperdao.utils.IntIdAll
import com.googlecode.mapperdao.QueryConfig

/**
 * @author kostantinos.kougios
 *
 * 7 Dec 2011
 */
abstract class ComputerDao extends TransactionalIntIdCRUD[Computer] with IntIdAll[Computer] {
	import com.googlecode.mapperdao.Query._

	// aliases for queries/joins
	private val ce = ComputerEntity
	private val co = CompanyEntity

	def pageOrderedBy(pageNumber: Long, rowsPerPage: Long, order: Int) = {
		val q = select from ce join (ce, ce.company, co) orderBy (order match {
			case 2 => (ce.name, asc)
			case -2 => (ce.name, desc)
			case 3 => (ce.introduced, asc)
			case -3 => (ce.introduced, desc)
			case 4 => (ce.discontinued, asc)
			case -4 => (ce.discontinued, desc)
			case 5 => (co.name, asc)
			case -5 => (co.name, desc)
		})
		queryDao.query(QueryConfig.pagination(pageNumber, rowsPerPage), q)
	}
}