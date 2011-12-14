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

	private val ce = ComputerEntity
	private val allByNameQuery = select from ce orderBy ce.name

	def pageOrderedByName(pageNumber: Long, rowsPerPage: Long) = queryDao.query(QueryConfig.pagination(pageNumber, rowsPerPage), allByNameQuery)
}