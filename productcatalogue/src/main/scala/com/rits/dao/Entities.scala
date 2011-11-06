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
object Entities {

	/**
	 * the product entity is the central entity of this example.
	 */
	object ProductEntity extends Entity[IntId, Product](classOf[Product]) {
		val id = intAutoGeneratedPK("id", _.id)
		val title = string("title", _.title)
		val description = string("description", _.description)
		// a product contains many prices for different currencies
		val prices = oneToMany(PriceEntity, _.prices)
		// products contain 0..n attributes
		val attributes = manyToMany(AttributeEntity, _.attributes)
		// products contain 0..n categories
		val categories = manyToMany(CategoryEntity, _.categories)
		// a simple type mapping for a string value. the Set[String] is
		// mapped via the TagsEntity (declared below)
		val tags = oneToManySimpleTypeString(TagsEntity, _.tags)
		/**
		 * this method constructs a Product instance when it is loaded from the database. The
		 * implicit 'm' variable is used to convert all the above columns into their equivalent
		 * values in a typesafe manner.
		 */
		def constructor(implicit m: ValuesMap) = new Product(title, description, prices, attributes, categories, tags) with IntId with Persisted {
			val id: Int = ProductEntity.id // converted implicitly using 'm'
		}
	}

	/**
	 * tags is a simple StringEntity that maps to "Tags" table, the FK to the Product table is the "product_id" column
	 * and the value is stored in the "tag" column.
	 */
	object TagsEntity extends StringEntity("Tags", "product_id", "tag")

	/**
	 * Categories are hierarchical. A category might have a parent category. We
	 * map this here.
	 */
	object CategoryEntity extends Entity[IntId, Category](classOf[Category]) {
		val id = intAutoGeneratedPK("id", _.id)
		val name = string("name", _.name)
		// self reference 
		val parent = manyToOneOption("parent_id", CategoryEntity, _.parent)

		def constructor(implicit m: ValuesMap) = new Category(name, parent) with Persisted with IntId {
			val id: Int = CategoryEntity.id
		}
	}

	object AttributeEntity extends Entity[IntId, Attribute](classOf[Attribute]) {
		val id = intAutoGeneratedPK("id", _.id)
		val name = string("name", _.name)
		val value = string("value", _.value)
		def constructor(implicit m: ValuesMap) = new Attribute(name, value) with IntId with Persisted {
			val id: Int = AttributeEntity.id
		}
	}

	/**
	 * prices exist for products. The table contains a product_id column
	 * and the primary key is (product_id,currency). This entity is not
	 * to be managed on it's own. It exists to be part of ProductEntity.
	 */
	object PriceEntity extends SimpleEntity[Price](classOf[Price]) {
		val currency = string("currency", _.currency)
		val unitPrice = double("unitprice", _.unitPrice)
		val salePrice = double("saleprice", _.salePrice)

		// this entity doesn't declare any primary keys.
		// we will inform mapperdao of the primary keys of the table below:
		val pks = declarePrimaryKeys("currency", "product_id")

		def constructor(implicit m: ValuesMap) = new Price(currency, unitPrice, salePrice) with Persisted
	}
}
