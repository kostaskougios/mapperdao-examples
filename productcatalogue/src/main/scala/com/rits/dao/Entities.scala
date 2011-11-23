package com.rits.dao
import com.googlecode.mapperdao.Entity
import com.googlecode.mapperdao.IntId
import com.rits.model.Category
import com.googlecode.mapperdao.Persisted
import com.googlecode.mapperdao.ValuesMap
import com.rits.model.Attribute
import com.rits.model.Price
import com.rits.model.Product
import com.googlecode.mapperdao.SimpleEntity
import com.googlecode.mapperdao.SimpleColumn
import com.googlecode.mapperdao.StringEntity

/**
 * entity declarations for all domain classes
 *
 * @author kostantinos.kougios
 *
 * 10 Oct 2011
 */

/**
 * the product entity is the central entity of this example.
 */
object ProductEntity extends Entity[IntId, Product](classOf[Product]) {
	val id = key("id") autogenerated (_.id)
	val title = column("title") to (_.title)
	val description = column("description") to (_.description)
	// a product contains many prices for different currencies
	val prices = onetomany(PriceEntity) to (_.prices)
	// products contain 0..n attributes
	val attributes = manytomany(AttributeEntity) to (_.attributes)
	// products contain 0..n categories
	val categories = manytomany(CategoryEntity) to (_.categories)

	/**
	 * tags is a simple StringEntity that maps to "Tags" table, the FK to the Product table is the "product_id" column
	 * and the value is stored in the "tag" column.
	 */
	val TagsEntity = StringEntity.oneToMany("Tags", "product_id", "tag")

	// a simple type mapping for a string value. the Set[String] is
	// mapped via the TagsEntity (declared below)
	val tags = onetomany(TagsEntity) tostring (_.tags)
	/**
	 * this method constructs a Product instance when it is loaded from the database. The
	 * implicit 'm' variable is used to convert all the above columns into their equivalent
	 * values in a typesafe manner.
	 */
	def constructor(implicit m) = new Product(title, description, prices, attributes, categories, tags) with IntId with Persisted {
		val id: Int = ProductEntity.id // converted implicitly using 'm'
	}

}

/**
 * Categories are hierarchical. A category might have a parent category. We
 * map this here.
 */
object CategoryEntity extends Entity[IntId, Category](classOf[Category]) {
	val id = key("id") autogenerated (_.id)
	val name = column("name") to (_.name)
	// self reference 
	val parent = manytoone(CategoryEntity) foreignkey "parent_id" option (_.parent) // _.parent is an Option[Category]

	def constructor(implicit m) = new Category(name, parent) with Persisted with IntId {
		val id: Int = CategoryEntity.id
	}
}

object AttributeEntity extends Entity[IntId, Attribute](classOf[Attribute]) {
	val id = key("id") autogenerated (_.id)
	val name = column("name") to (_.name)
	val value = column("value") to (_.value)
	def constructor(implicit m) = new Attribute(name, value) with IntId with Persisted {
		val id: Int = AttributeEntity.id
	}
}

/**
 * prices exist for products. The table contains a product_id column
 * and the primary key is (product_id,currency). This entity is not
 * to be managed on it's own. It exists to be part of ProductEntity.
 */
object PriceEntity extends SimpleEntity[Price](classOf[Price]) {
	val currency = column("currency") to (_.currency)
	val unitPrice = column("unitprice") to (_.unitPrice)
	val salePrice = column("saleprice") to (_.salePrice)

	// this entity doesn't declare any primary keys.
	// we will inform mapperdao of the primary keys of the table below:
	val pks = declarePrimaryKeys("currency", "product_id")

	def constructor(implicit m) = new Price(currency, unitPrice, salePrice) with Persisted
}
