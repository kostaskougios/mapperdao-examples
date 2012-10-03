package dellstore.dao

import com.googlecode.mapperdao._
import dellstore.model._

/**
 * maps to this table:
 *
 * CREATE TABLE categories
 * (
 * category serial NOT NULL,
 * categoryname character varying(50) NOT NULL,
 * CONSTRAINT categories_pkey PRIMARY KEY (category )
 * )
 */
object CategoryEntity extends Entity[Int, SurrogateIntId, Category]("Categories") {
	val id = key("category") autogenerated (_.id)
	val name = column("categoryname") to (_.name)

	def constructor(implicit m) = new Category(name) with SurrogateIntId {
		val id: Int = CategoryEntity.id
	}
}
