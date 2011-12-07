package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.validation.Constraints._
import views._
import models._
import dao.Daos._

/**
 * Manage a database of computers
 */
object Application extends Controller {

	/**
	 * This result directly redirect to the application home.
	 */
	val Home = Redirect(routes.Application.list(0, 2, ""))

	/**
	 * Describe the computer form (used in both edit and create screens).
	 */
	val computerForm = Form(
		of(Computer.apply _)(
			"id" -> ignored(NotAssigned),
			"name" -> requiredText,
			"introduced" -> optional(date("yyyy-MM-dd")),
			"discontinued" -> optional(date("yyyy-MM-dd")),
			"company" -> optional(number)
		)
	)

	// -- Actions

	/**
	 * Handle default path requests, redirect to computers list
	 */
	def index = Action { Home }

	/**
	 * Display the paginated list of computers.
	 *
	 * @param page Current page number (starts from 0)
	 * @param orderBy Column to be sorted
	 * @param filter Filter applied on computer names
	 */
	def list(page: Int, orderBy: Int, filter: String) = Action { implicit request =>
		Ok(html.list(
			computerDao.page(page, 10),
			orderBy, filter
		))
	}

	/**
	 * Display the 'edit form' of a existing Computer.
	 *
	 * @param id Id of the computer to edit
	 */
	def edit(id: Int) = Action {
		computerDao.retrieve(id).map { computer =>
			Ok(html.editForm(id, computerForm.fill(computer)))
		}.getOrElse(NotFound)
	}

	/**
	 * Handle the 'edit form' submission
	 *
	 * @param id Id of the computer to edit
	 */
	def update(id: Long) = Action { implicit request =>
		computerForm.bindFromRequest.fold(
			formWithErrors => BadRequest(html.editForm(id, formWithErrors)),
			computer => {
				Computer.update(id, computer)
				Home.flashing("success" -> "Computer %s has been updated".format(computer.name))
			}
		)
	}

	/**
	 * Display the 'new computer form'.
	 */
	def create = Action {
		Ok(html.createForm(computerForm))
	}

	/**
	 * Handle the 'new computer form' submission.
	 */
	def save = Action { implicit request =>
		computerForm.bindFromRequest.fold(
			formWithErrors => BadRequest(html.createForm(formWithErrors)),
			computer => {
				Computer.insert(computer)
				Home.flashing("success" -> "Computer %s has been created".format(computer.name))
			}
		)
	}

	/**
	 * Handle computer deletion.
	 */
	def delete(id: Int) = Action {
		computerDao.delete(id)
		Home.flashing("success" -> "Computer has been deleted")
	}

}
