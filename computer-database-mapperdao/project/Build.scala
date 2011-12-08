import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "computer-database-mapperdao"
    val appVersion      = "1.0"

    val appDependencies = Seq("com.googlecode.mapperdao" % "mapperdao" % "0.9.2")

    val main = PlayProject(appName, appVersion, appDependencies).settings(defaultScalaSettings:_*).settings(
      // Add your own project settings here      
    )

}
            
