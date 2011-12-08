resolvers ++= Seq(
    DefaultMavenRepository,
    //"Local Maven Repository" at "file://" + (Path.userHome / ".m2" / "repository").absolutePath,
    Resolver.url("Play", url("http://download.playframework.org/ivy-releases/"))(Resolver.ivyStylePatterns),
    "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

libraryDependencies ++= Seq ( 
	"play" %% "play" % "2.0-beta"
)