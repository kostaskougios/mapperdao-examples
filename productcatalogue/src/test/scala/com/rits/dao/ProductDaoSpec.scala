package com.rits.dao
import com.rits.model._
import org.specs2.mutable.SpecificationWithJUnit

/**
 * @author kostantinos.kougios
 *
 * 10 Oct 2011
 */
class ProductDaoSpec extends SpecificationWithJUnit {
	val productsDao = Daos.productsDao
	val attributesDao = Daos.attributesDao

	"persist" in {
		val attributes = Set(Attribute("performance", "fast"), Attribute("security", "extra secure"))
		val categories = List(Category("Linux", Some(Category("Operating Systems", None))))
		val p = Product("test1", "desc1", Set(Price("GBP", 10.5, 9.99), Price("EUR", 12.50, 11.05)), attributes, categories, Set())
		val created = productsDao.create(p)
		val retrieved = productsDao.retrieve(created.id).get
		retrieved must_== created
	}
}