import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

	val appName = "computer-database-mapperdao"
	val appVersion = "1.0"

	val appDependencies = Seq(
			"com.googlecode.mapperdao" % "mapperdao" % "1.0.0-beta",
			"joda-time" % "joda-time" % "1.6.2",
			"org.joda" % "joda-convert" % "1.2"
	)

	val main = PlayProject(appName, appVersion, appDependencies).settings(defaultScalaSettings: _*).settings(
		// Add your own project settings here
		resolvers += (
			"Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"
		),
		scalacOptions ++= Seq("-Xexperimental")
	)

}
            
