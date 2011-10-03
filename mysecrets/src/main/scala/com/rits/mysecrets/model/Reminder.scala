package com.rits.mysecrets.model
import org.joda.time.DateTime

/**
 * the reminder is a hierarchy of classes and it demonstrates
 * the capability of mapperdao to store a class hierarchy
 * into 1 table
 *
 * @author kostantinos.kougios
 *
 * 22 Sep 2011
 */
abstract class Reminder(val remindUsers: Set[User])

case class Daily(hourOfDay: Short, override val remindUsers: Set[User]) extends Reminder(remindUsers)
case class Weekly(hourOfDay: Short, dayOfWeek: Short, override val remindUsers: Set[User]) extends Reminder(remindUsers)
case class RemindOnce(time: DateTime, override val remindUsers: Set[User]) extends Reminder(remindUsers)