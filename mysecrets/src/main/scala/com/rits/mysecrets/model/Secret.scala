package com.rits.mysecrets.model

/**
 * stores secrets of a user
 * A secret belongs to a user and can be shared with other users
 *
 * @author kostantinos.kougios
 *
 * 16 Sep 2011
 */
class Secret(val title: String, val secret: String, val user: User, val sharedWith: Set[User]) {

	override def toString = "Secret(%s, sharing with %d users)".format(title, sharedWith.size)
}