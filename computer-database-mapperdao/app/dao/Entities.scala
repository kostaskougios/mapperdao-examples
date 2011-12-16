package dao
import com.googlecode.mapperdao.Entity
import com.googlecode.mapperdao.IntId
import models.Company
import com.googlecode.mapperdao.Persisted
import models.Computer

/**
 * @author kostantinos.kougios
 *
 * 7 Dec 2011
 */
object CompanyEntity extends Entity[IntId, Company](classOf[Company]) {
	val id = key("id") autogenerated (_.id)
	val name = column("name") to (_.name)

	def constructor(implicit m) = new Company(name) with IntId with Persisted {
		val id: Int = CompanyEntity.id
	}
}

object ComputerEntity extends Entity[IntId, Computer](classOf[Computer]) {
	val id = key("id") autogenerated (_.id)
	val name = column("name") to (_.name)
	val introduced = column("introduced") option (_.introduced)
	val discontinued = column("discontinued") option (_.discontinued)
	val company = manytoone(CompanyEntity) option (_.company)

	def constructor(implicit m) = new Computer(name, introduced, discontinued, company) with IntId with Persisted {
		val id: Int = ComputerEntity.id
	}
}