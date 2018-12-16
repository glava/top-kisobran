import sbtcrossproject.{crossProject, CrossType}
val slickV = "3.2.1"
newrelicVersion := "4.5.0"
lazy val server = (project in file("server")).settings(commonSettings).settings(
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http" % "10.1.1",
    "com.typesafe.akka" %% "akka-stream" % "2.5.11",
    "com.typesafe.akka" %% "akka-http-testkit" % "10.1.1" % Test,
    "com.vmunier" %% "scalajs-scripts" % "1.1.2",
    "org.liquibase" % "liquibase-core" % "3.6.1",
    "com.chuusai"        %% "shapeless" % "2.3.3",
    "io.underscore"      %% "slickless" % "0.3.3",
    "com.typesafe.slick" %% "slick" % slickV,
    "com.h2database" % "h2" % "1.4.196",
    "com.typesafe.slick" %% "slick-codegen" % slickV,
    "com.typesafe.slick" %% "slick-hikaricp" % slickV,
    "org.scalatest" %% "scalatest" % "3.0.3" % Test,
    "org.postgresql" % "postgresql" % "42.0.0",
    "com.newrelic.agent.java" % "newrelic-api" % "4.8.0"
  ),
  WebKeys.packagePrefix in Assets := "public/",
  managedClasspath in Runtime += (packageBin in Assets).value,
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for Twirl templates are present
  EclipseKeys.preTasks := Seq(compile in Compile)
).enablePlugins(SbtWeb, SbtTwirl, JavaAppPackaging, NewRelic).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(commonSettings).settings(
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.5"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(commonSettings)
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val commonSettings = Seq(
  scalaVersion := "2.12.5",
  organization := "org.kisobran"
)

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}
