package com.rits.mysecrets.snippet

import scala.xml._
import net.liftweb.util.Helpers
import Helpers._
import net.liftweb.http._
import SHtml._
import S._
import com.rits.mysecrets.Daos._
import com.rits.mysecrets.model.Secret
import com.googlecode.mapperdao.IntId
import net.liftweb.common.Logger
import net.liftweb.common.Full
import com.rits.mysecrets.model.User
import com.rits.mysecrets.model.Reminder
import com.rits.mysecrets.model.Daily
import com.rits.mysecrets.model.Weekly
import com.rits.mysecrets.model.RemindOnce
import org.joda.time.format.ISODateTimeFormat
import net.liftweb.common.Empty
import org.joda.time.DateTime

/**
 * list, view and edit secrets
 *
 * @author kostantinos.kougios
 *
 * 17 Sep 2011
 */
class Secrets extends Logger {
	object secretVar extends RequestVar[Option[Secret with IntId]](None)

	def list(in: NodeSeq): NodeSeq = {
		val user = UserVar.get.get // get user from session
		val secrets = secretDao.all(user) // find all secrets of user
		val sharedSecrets = secretDao.sharedWith(user) // find all secrets other users share with this user

			// binds the secrets of this user
			def bindSecrets(template: NodeSeq): NodeSeq = secrets.flatMap { secret =>
				bind("secret", template,
					"title" -> SHtml.link("view.html", () => secretVar(Some(secret)), Text(secret.title)),
					"shared" -> (if (secret.sharedWith.isEmpty) "no" else "yes"),
					"edit" -> SHtml.link("edit.html", () => secretVar(Some(secret)), Text("Edit")),
					"delete" -> SHtml.link("list.html", () => secretDao.delete(secret), Text("Delete"))
				)
			}

			// binds the secrets shared by other users
			def bindSharedSecrets(template: NodeSeq): NodeSeq = sharedSecrets.flatMap { secret =>
				bind("secret", template,
					"title" -> SHtml.link("view.html", () => secretVar(Some(secret)), Text(secret.title)),
					"by" -> secret.user.name
				)
			}

		bind("secret", in, "list" -> bindSecrets _, "shared" -> bindSharedSecrets _)
	}

	def view(in: NodeSeq): NodeSeq = {
		val secret = secretVar.get.get // the secretVar definately contains a secret
			def bindSharedWith(template: NodeSeq): NodeSeq = secret.sharedWith.flatMap { user =>
				bind("user", template, "name" -> user.name, "email" -> user.email)
			}.toSeq

		bind("secret", in,
			"id" -> secret.id,
			"title" -> secret.title,
			"secret" -> secret.secret,
			"sharedWith" -> bindSharedWith _,
			// allow editing only if the user owns the secret (oh well, this is easy to hack but I won't complicate things)
			if (secret.user == UserVar.get) "edit" -> SHtml.link("edit.html", () => secretVar(Some(secret)), Text("Edit")) else "edit" -> "Edit"
		)
	}

	def edit(in: NodeSeq): NodeSeq = {
		object titleVar extends RequestVar("")
		object secretTitleVar extends RequestVar("")
		object sharedWithVar extends RequestVar(scala.collection.mutable.Set[User]())
		object remindersVar extends RequestVar(scala.collection.mutable.Set[Reminder]())
		object isNewVar extends RequestVar(!secretVar.get.isDefined)
			// a list of constraints for validation
			def validation = List(
				(titleVar.isEmpty(), "Please enter a title for the secret."),
				(secretTitleVar.isEmpty(), "Please enter description for the secret")
			)

			def getUpdatedSecret = {
				val user = UserVar.get.get // get user from session
				new Secret(titleVar.get, secretTitleVar.get, user, sharedWithVar.get.toSet, remindersVar.get.toSet) with IntId {
					val id = -1
				}
			}
		// now we'll decide if we edit an existing
		// secret or create a new one. The submit
		// button & callbacks are modified accordingly
		val subm = if (isNewVar.get) {
			// Create a new secret
			info("Creating a new secret.")
			submit("Save", () => Validation.onValidation({
				// create the instance of the new secret
				val user = UserVar.get.get // get user from session
				val s = new Secret(titleVar.get, secretTitleVar.get, user, sharedWithVar.get.toSet, remindersVar.get.toSet)
				// insert it into the database
				val newSecret = secretDao.create(s)
				info("Created Secret (%d : %s)".format(newSecret.id, newSecret))
				redirectTo("list")
			}, validation))
		} else {
			val oldSecret = secretVar.get.get
			// editing an existing secret
			info("Updating secret (%d : %s)".format(oldSecret.id, oldSecret))
			// set the request variables from the secret properties
			if (!request.get.post_?) {
				titleVar.set(oldSecret.title)
				secretTitleVar.set(oldSecret.secret)
				sharedWithVar(sharedWithVar.get ++= oldSecret.sharedWith)
			}
			// on submit, we'll update the secret to the
			// database using the secretDao. Since we are
			// updating an immutable class, we need to copy
			// over the previous values and modify them
			// accordingly.
			submit("Update", () => Validation.onValidation({
				// update database. We need both the oldSecret and the new updatedSecret instances.
				val us = getUpdatedSecret
				val updated = secretDao.update(oldSecret, us)
				info("Secret updated from %s to %s".format(oldSecret, updated))
				redirectTo("list")
			}, validation))
		}

			// Sharing secrets.
			// it is important to iterate through the original
			// sharedWith set, because we want to modify it
			def bindSharedWithUsers(template: NodeSeq): NodeSeq = (sharedWithVar.get ++ userDao.allBut(UserVar.get.get).toSet).toList.flatMap { user =>
				bind("edit", template,
					"userName" -> user.name,
					"emailCheckbox" -> checkbox(
						// if user is contained in the set, the checkbox should be checked
						sharedWithVar.get.contains(user),
						if (_)
							// for every checked checkbox, we'll add the user to the shared set.
							sharedWithVar.get += user
						else
							// for every unchecked checkbox, we'll remove the user from the shared set.
							sharedWithVar.get -= user
					)
				)
			}
			// binds the reminders
			def bindReminders(template: NodeSeq): NodeSeq = remindersVar.get.toList.flatMap { reminder =>
				object reminderTypeVar extends RequestVar("")
				var hourOfDay = ""
				var dayOfWeek = ""
				var time = ""
				bind("reminder", template,
					"type" -> select(
						List(("daily", "Daily"), ("weekly", "Weekly"), ("once", "Once")),
						reminder match {
							case _: Daily => Full("daily")
							case _: Weekly => Full("weekly")
							case _: RemindOnce => Full("once")
						},
						reminderTypeVar(_)),
					"hourOfDay" -> text((reminder match {
						case Daily(hourOfDay, _) => hourOfDay
						case Weekly(hourOfDay, _, _) => hourOfDay
						case RemindOnce(time, _) => time.getHourOfDay
					}).toString, hourOfDay = _),
					"dayOfWeek" -> text((reminder match {
						case _: Daily => -1
						case Weekly(_, dayOfWeek, _) => dayOfWeek
						case RemindOnce(time, _) => time.getDayOfWeek
					}).toString, dayOfWeek = _),
					"time" -> text((reminder match {
						case _: Daily => ""
						case _: Weekly => ""
						case RemindOnce(time, _) => time.toString(ISODateTimeFormat.dateTime)
					}), t => {
						time = t

						val newReminder = reminderTypeVar.get match {
							case "daily" => new Daily(hourOfDay.toShort, Set())
							case "weekly" => new Weekly(hourOfDay.toShort, dayOfWeek.toShort, Set())
							case "once" =>
								val fmt = ISODateTimeFormat.dateTime()
								new RemindOnce(fmt.parseDateTime(time), Set())
						}
						if (newReminder != reminder) {
							remindersVar.get -= reminder
							remindersVar.get += newReminder
						}
					})
				)
			}

		bind(
			"edit", in,
			"title" -> text(titleVar, titleVar(_)),
			"secret" -> textarea(secretTitleVar, secretTitleVar(_)),
			"users" -> bindSharedWithUsers _,
			"reminders" -> bindReminders _,
			"addReminder" -> submit("Add Reminder", () => {
				remindersVar.get += Daily(10, Set())
			}),
			"submit" -> subm
		)
	}
}