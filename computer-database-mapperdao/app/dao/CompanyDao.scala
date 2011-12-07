package dao
import com.googlecode.mapperdao.utils.IntIdAll
import com.googlecode.mapperdao.utils.TransactionalIntIdCRUD
import models.Company

/**
 * @author kostantinos.kougios
 *
 * 7 Dec 2011
 */
abstract class CompanyDao extends TransactionalIntIdCRUD[Company] with IntIdAll[Company] {
}