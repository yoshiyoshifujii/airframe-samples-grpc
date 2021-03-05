lazy val baseName = "airframe-samples-grpc"

val AIRFRAME_VERSION = "21.2.0"

val buildSettings = Seq(
  organization := "com.github.yoshiyoshifujii",
  scalaVersion := "2.13.5",
  version := "0.1"
)

lazy val api =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file(s"$baseName-api"))
    .settings(
      buildSettings,
      libraryDependencies ++= Seq(
        "org.wvlet.airframe" %%% "airframe-http" % AIRFRAME_VERSION
      )
    )

lazy val apiJVM = api.jvm
lazy val apiJS  = api.js

lazy val server =
  project
    .in(file(s"$baseName-server"))
    .settings(
      buildSettings,
      libraryDependencies ++= Seq(
        "org.wvlet.airframe" %% "airframe-http-finagle" % AIRFRAME_VERSION,
        "org.wvlet.airframe" %% "airframe-http-grpc"    % AIRFRAME_VERSION
      )
    )
    .dependsOn(apiJVM)

lazy val client =
  project
    .in(file(s"$baseName-client"))
    .enablePlugins(AirframeHttpPlugin)
    .settings(
      buildSettings,
      airframeHttpClients := Seq(s"$baseName.app.v1:sync", "$baseName.app.v1:async"),
      airframeHttpGeneratorOption := "-l debug",
      libraryDependencies ++= Seq(
        "org.wvlet.airframe" %% "airframe-http-finagle" % AIRFRAME_VERSION,
        "org.wvlet.airframe" %% "airframe-http-grpc"    % AIRFRAME_VERSION
      )
    )
    .dependsOn(apiJVM)

lazy val ui =
  project
    .in(file(s"$baseName-ui"))
    .enablePlugins(ScalaJSPlugin, AirframeHttpPlugin)
    .settings(
      buildSettings,
      airframeHttpClients := Seq(s"$baseName.app.v1:scalajs"),
      airframeHttpGeneratorOption := "-l debug"
    )
    .dependsOn(apiJS)