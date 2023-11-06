import sbt.io.Path.userHome

name := "kotlin-plugin"
organization := "com.github.alxbun"
version := "2.0.3-sbt-1.3"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))
homepage := Some(url("https://github.com/alxbun/kotlin-plugin"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/alxbun/kotlin-plugin"),
    "scm:git:https://github.com/alxbun/kotlin-plugin.git",
    "scm:git:git@github.com/alxbun/kotlin-plugin.git"
  )
)

lazy val LocalMavenResolverForSbtPlugins = {
  // remove scala and sbt versions from the path, as it does not work with jitpack
  val pattern  = "[organisation]/[module]/[revision]/[module]-[revision](-[classifier]).[ext]"
  val name     = "local-maven-for-sbt-plugins"
  val location = userHome / ".m2" / "repository"
  Resolver.file(name, location)(Patterns().withArtifactPatterns(Vector(pattern)))
}

sbtPlugin := true
scalacOptions ++= Seq("-deprecation","-Xlint","-feature")
libraryDependencies ++= Seq(
  "io.argonaut" %% "argonaut" % "6.3.8",
  "org.scalaz" %% "scalaz-core" % "7.3.7"
)

enablePlugins(BuildInfoPlugin, SbtPlugin)
buildInfoPackage := "kotlin"
scriptedLaunchOpts ++= Seq(
  "-Xmx1024m",
  "-Dplugin.org=" + organization.value,
  "-Dplugin.name=" + name.value,
  "-Dplugin.version=" + version.value)

// Publishing
publishMavenStyle := true
resolvers += LocalMavenResolverForSbtPlugins
publishM2Configuration := publishM2Configuration.value.withResolverName(LocalMavenResolverForSbtPlugins.name)

resolvers += Resolver.mavenLocal