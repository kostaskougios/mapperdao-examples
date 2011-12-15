package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.validation.Constraints._
import views._
import models._
import dao.Daos._
import org.scala_tools.time.Imports._
import java.util.Date

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

	// we'll create this here since it is specific to the controller rather than the domain class

	val computerForm = Form(
		of((name: String, introduced: Option[Date], discontinued: Option[Date], companyId: Option[Long]) =>
			Computer(name, introduced, discontinued, companyId.map(id => companyDao.retrieve(id.toInt).get))
		)(
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
			Page(
				computerDao.pageOrderedBy(page + 1, 10, orderBy).map(computer => (computer, computer.company)),
				page, 10 * page, computerDao.countAll
			),
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
			Ok(html.editForm(id, computerForm.fill(computer), companyDao.all.map(c => (c.id.toString, c.name))))
		}.getOrElse(NotFound)
	}

	/**
	 * Handle the 'edit form' submission
	 *
	 * @param id Id of the computer to edit
	 */
	def update(id: Int) = Action { implicit request =>
		computerForm.bindFromRequest.fold(
			formWithErrors => BadRequest(html.editForm(id, formWithErrors, allCompaniesForView)),
			computer => {
				val oldV = computerDao.retrieve(id).get
				computerDao.update(oldV, computer)
				Home.flashing("success" -> "Computer %s has been updated".format(computer.name))
			}
		)
	}

	def allCompaniesForView = companyDao.all.map(company => (company.id.toString, company.name))
	/**
	 * Display the 'new computer form'.
	 */
	def create = Action {
		Ok(html.createForm(computerForm, allCompaniesForView))
	}

	/**
	 * Handle the 'new computer form' submission.
	 */
	def save = Action { implicit request =>
		computerForm.bindFromRequest.fold(
			formWithErrors => BadRequest(html.createForm(formWithErrors, allCompaniesForView)),
			computer => {
				computerDao.create(computer)
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
