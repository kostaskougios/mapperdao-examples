package com.rits.dao
import com.rits.model._
import org.specs2.mutable.SpecificationWithJUnit
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

/**
 * @author kostantinos.kougios
 *
 * 10 Oct 2011
 */
@RunWith(classOf[JUnitRunner])
class ProductDaoSpec extends SpecificationWithJUnit {

	val productsDao = Daos.productsDao
	val attributesDao = Daos.attributesDao
	val categoriesDao = Daos.categoriesDao

	"update prices" in {
		val p = Product("test1", "desc1", Set(Price("GBP", 10.5, 9.99), Price("EUR", 12.50, 11.05)), Set(), List(), Set())
		val created = productsDao.create(p)

		val newP = new Product(created.title, created.description, created.prices.filterNot(_.currency == "GBP"), created.attributes, created.categories, created.tags)
		val updated = productsDao.update(created, newP)
		updated must_== newP
	}

	"persist attributes" in {
		val a1 = attributesDao.getOrCreate("performance", "fast")
		val a2 = attributesDao.getOrCreate("security", "extra secure")
		val p = productsDao.create(Product("test1", "desc1", Set(), Set(a1, a2), List(), Set()))
		productsDao.retrieve(p.id).get.attributes must_== Set(a1, a2)
	}

	"update attributes" in {
		val a1 = attributesDao.getOrCreate("performance", "fast")
		val a2 = attributesDao.getOrCreate("security", "extra secure")
		val p = productsDao.create(Product("test1", "desc1", Set(), Set(a1, a2), List(), Set()))
		val u = productsDao.update(p, Product(p.title, p.description, Set(), Set(a1), List(), Set()))
		productsDao.retrieve(u.id).get.attributes must_== Set(a1)
	}

	"persist categories" in {
		val cat1 = categoriesDao.createHierarchy(List("Operating Systems", "iMac"))
		val cat2 = categoriesDao.createHierarchy(List("Operating Systems", "iMac", "Leopard"))
		val categories = List(cat1, cat2)
		val p = productsDao.create(Product("test1", "desc1", Set(), Set(), categories, Set()))
		productsDao.retrieve(p.id).get.categories.toSet must_== Set(cat1, cat2)
	}

	"update categories" in {
		val cat1 = categoriesDao.createHierarchy(List("Operating Systems", "iMac"))
		val cat2 = categoriesDao.createHierarchy(List("Operating Systems", "iMac", "Leopard"))
		val categories = List(cat1, cat2)
		val p = productsDao.create(Product("test1", "desc1", Set(), Set(), categories, Set()))
		val u = productsDao.update(p, Product("test1", "desc1", Set(), Set(), List(cat1), Set()))
		productsDao.retrieve(p.id).get.categories.toSet must_== Set(cat1)
	}

	"persist combined" in {
		val a1 = attributesDao.getOrCreate("performance", "fast")
		val a2 = attributesDao.getOrCreate("security", "extra secure")
		val attributes = Set(a1, a2)

		val cat = categoriesDao.createHierarchy(List("Operating Systems", "Linux"))
		val categories = List(cat)
		val p = Product("test1", "desc1", Set(Price("GBP", 10.5, 9.99), Price("EUR", 12.50, 11.05)), attributes, categories, Set("processor", "3000ghz"))
		val created = productsDao.create(p)
		created must_== p
		val retrieved = productsDao.retrieve(created.id).get
		retrieved must_== created
	}

	"update combined" in {
		val a1 = attributesDao.getOrCreate("performance", "fast")
		val a2 = attributesDao.getOrCreate("security", "extra secure")
		val attributes = Set(a1, a2)

		val cat1 = categoriesDao.createHierarchy(List("Operating Systems", "Linux"))
		val cat2 = categoriesDao.createHierarchy(List("Operating Systems", "Leopard"))
		val categories = List(cat1, cat2)
		val p = productsDao.create(Product("test1", "desc1", Set(Price("GBP", 10.5, 9.99), Price("EUR", 12.50, 11.05)), attributes, categories, Set("processor", "3000ghz")))
		val u = productsDao.update(p, Product("test1", "desc1", p.prices.filterNot(_.currency == "GBP"), Set(a1), List(cat2), Set("processor", "2000ghz")))
		val r = productsDao.retrieve(u.id).get
		r.prices must_== Set(Price("EUR", 12.50, 11.05))
		r.attributes must_== Set(a1)
		r.categories must_== List(cat2)
		r.tags must_== Set("processor", "2000ghz")
	}
}