import sbt.Project.projectToRef

import scala.language.postfixOps

///////DISABLE FOR PRODUCTION
lazy val development = true

def commonSettings(pname: String) = Seq(
  version := "0.1-SNAPSHOT",
  organization := "net.magik6k",
  scalaVersion := "2.11.7",
  name := pname
)

//////////////////////////
// SERVER

lazy val clients = Seq(client)
lazy val server = (project in file("server")).settings(commonSettings("server"): _*).settings(
  scalaJSProjects := clients,
  pipelineStages := Seq(scalaJSProd, gzip),
  name := """routerware""",
  resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  libraryDependencies ++= Seq(
  	"com.vmunier" %% "play-scalajs-scripts" % "0.3.0",
    "org.webjars" % "jquery" % "1.11.1",
    //"me.chrons" %% "boopickle" % "1.1.0",
    cache,
    ws,
    specs2 % Test
  ),
  LessKeys.compress in Assets := true,
  includeFilter in (Assets, LessKeys.less) := "main.less",
  routesGenerator := InjectedRoutesGenerator,

  (compile in Compile) <<= (compile in Compile)
).enablePlugins(PlayScala).
  aggregate(clients.map(projectToRef): _*)
  .dependsOn(commonJvm)

//////////////////////////
// CLIENT

lazy val client = (project in file("client")).settings(commonSettings("client"): _*).settings(
  persistLauncher := true,
  persistLauncher in Test := false,
  emitSourceMaps in fullOptJS := development,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0"
    //"me.chrons" %%% "boopickle" % "1.1.0"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSPlay)
  .dependsOn(commonJs)
  .dependsOn(simpletags)
  .dependsOn(window)

//////////////////////////
// SJS WINDOWS

lazy val window = (project in file("window")).settings(commonSettings("window"): _*).settings(
  emitSourceMaps in fullOptJS := development,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSPlay)
  .dependsOn(simpletags)
  .dependsOn(masj)

//////////////////////////
// TAGS LIB

lazy val simpletags = (project in file("simpletags")).settings(commonSettings("simpletags"): _*).settings(
  emitSourceMaps in fullOptJS := development,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSPlay)
  .dependsOn(commonJs)

//////////////////////////
// MASJ LIB

lazy val masj = (project in file("masj")).settings(commonSettings("masj"): _*).settings(
  emitSourceMaps in fullOptJS := development,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0",
    "be.doeraene" %%% "scalajs-jquery" % "0.8.0"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSPlay)

//////////////////////////
// GLOBAL COMMON

lazy val common = (crossProject.crossType(CrossType.Pure) in file("common")).
  settings(commonSettings("common"): _*).
  jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val commonJvm = common.jvm.settings(
  libraryDependencies ++= Seq(
    //"me.chrons" %% "boopickle" % "1.1.0"
  )
)
lazy val commonJs = common.js.settings(
  libraryDependencies ++= Seq(
    //"me.chrons" %%% "boopickle" % "1.1.0"
  )
)

//////////////////////////
// NATIVE DEPS

lazy val netadmin = TaskKey[Unit]("netadmin", "Compiles netadmin and dependencies")

netadmin := {
  println("Preparing netadmin dependencies")
  "scripts/build-libnl.sh" !;
  "scripts/netadmin-deps.sh" !

  println("Compiling netadmin")
  "scripts/netadmin.sh" !
}

/////
// http://www.infradead.org/~tgr/libnl/

//////////////////////////
// OTHER

//onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value

