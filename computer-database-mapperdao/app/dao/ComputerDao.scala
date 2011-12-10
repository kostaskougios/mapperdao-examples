package dao
import com.googlecode.mapperdao.utils.TransactionalIntIdCRUD
import models.Computer
import com.googlecode.mapperdao.utils.IntIdAll

/**
 * @author kostantinos.kougios
 *
 * 7 Dec 2011
 */
abstract class ComputerDao extends TransactionalIntIdCRUD[Computer] with IntIdAll[Computer]