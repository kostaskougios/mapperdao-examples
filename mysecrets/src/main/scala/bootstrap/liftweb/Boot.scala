package bootstrap.liftweb

import net.liftweb.common.Logger
import net.liftweb.http.provider.HTTPRequest
import net.liftweb.http.LiftRules
import net.liftweb.http.S
import net.liftweb.sitemap.Loc.Hidden
import net.liftweb.sitemap.Loc
import net.liftweb.sitemap.Menu
import net.liftweb.sitemap.SiteMap
import net.liftweb.util.LoanWrapper
import com.rits.mysecrets.snippet.UserVar
import com.rits.mysecrets.Daos
import net.liftweb.sitemap.Loc.If
import net.liftweb.http.RedirectResponse

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
	def boot {
		// where to search snippet
		LiftRules.addToPackages("com.rits.mysecrets")

		val ifLogged = If(() => UserVar.get.isDefined, () => RedirectResponse("invalid"))
		// Build SiteMap
		// TODO: hide menus when they are not valid (user not logged in)
		val entries =
			Menu(Loc("Home", List("index"), "Home")) ::
				Menu(Loc("Test", List("test"), "Test Page")) ::
				Menu(Loc("Login", List("login"), "Login Page")) ::
				Menu(Loc("Register", List("register"), "Registration")) ::
				Menu(Loc("RegistrationSuccesfull", List("registration-succesfull"), "Registration Succesfull", Hidden)) ::
				// after login pages
				Menu(Loc("secrets/list", List("secrets", "list"), "Secrets List", ifLogged)) ::
				Menu(Loc("secrets/edit", List("secrets", "edit"), "Create a Secret", ifLogged)) ::
				Menu(Loc("secrets/view", List("secrets", "view"), "View a Secret", Hidden)) ::
				Nil
		LiftRules.setSiteMap(SiteMap(entries: _*))
		LiftRules.early.append(makeUtf8)
		LiftRules.enableLiftGC = false

		//		S.addAround(new LoanWrapper with Logger { // Y  
		//			def apply[T](f: => T): T = {
		//				// when in dev mode, autologin
		//				UserVar.get match {
		//					case null =>
		//						info("auto logging in")
		//						val user = Daos.userDao.byEmail("kostas.kougios@googlemail.com").get
		//						UserVar.set(user)
		//				}
		//				val result = f
		//				result
		//			}
		//		})
	}
	/**
	 * Force the request to be UTF-8
	 */
	private def makeUtf8(req: HTTPRequest) {
		req.setCharacterEncoding("UTF-8")
	}
}
