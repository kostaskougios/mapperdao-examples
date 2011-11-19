package com.rits.mysecrets.dao

import com.googlecode.mapperdao.Entity
import com.rits.mysecrets.model.Secret
import com.googlecode.mapperdao.IntId
import com.rits.mysecrets.model.User
import com.googlecode.mapperdao.Persisted
import com.googlecode.mapperdao.ValuesMap
import com.googlecode.mapperdao.utils.TransactionalIntIdCRUD
import com.googlecode.mapperdao.utils.IntIdAll
import com.googlecode.mapperdao.Query
import com.googlecode.mapperdao.QueryDao
import com.rits.mysecrets.model.Reminder

/**
 * @author kostantinos.kougios
 *
 * 16 Sep 2011
 */
abstract class SecretDao extends TransactionalIntIdCRUD[Secret] {
	// the query dao will be injected by Daos class
	protected val queryDao: QueryDao

	// the entity for this dao is the SecretEntity which is declared
	// at the companion object
	import SecretDao._
	protected val entity = SecretEntity

	private val s = SecretEntity
	private val u = UserDao.UserEntity
	import Query._
	import queryDao._

	/**
	 * get all secrets for this user
	 */
	def all(user: User): List[Secret with IntId] = query(
		select from s
			join (s, s.user, u)
			where s.user === user
	)

	/**
	 * get all secrets that are shared by
	 * others for this user
	 */
	def sharedWith(user: User): List[Secret with IntId] = query(
		select from s
			where s.sharedWith === user
	)
}

object SecretDao {
	/**
	 * Mapping for  the Secret class.
	 *
	 * The Secret domain model class will aquire an IntId after been persisted.
	 * The id is provided by the database as an autogenerated column.
	 */
	object SecretEntity extends Entity[IntId, Secret](classOf[Secret]) {
		val id = key("id") autogenerated ( _.id) // the autogenerated id mapped to "id" column and _.id property
		val title = column("title") to ( _.title)
		val secret = column("secret") to ( _.secret)
		val user = manytoone(UserDao.UserEntity) to ( _.user) // many secrets belong to 1 user 
		var sharedWith = manytomany(UserDao.UserEntity) to ( _.sharedWith) // many secrets can be shared amongst many users
		val reminders = onetomany(ReminderDao.ReminderEntity) to ( _.reminders)
		// instantiate the entity when it is read from the database
		def constructor(implicit m: ValuesMap) = new Secret(title, secret, user, sharedWith, reminders) with IntId with Persisted {
			val id: Int = SecretEntity.id // the id property is added to the entity after persisting it
		}
	}
}