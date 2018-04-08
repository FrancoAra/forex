
lazy val IntegrationTest = config("it") extend Test

lazy val forex = project.in(file("."))
  .configs(IntegrationTest extend Test)
  .settings(Defaults.itSettings)
  .settings(parallelExecution in IntegrationTest := false)
  .settings(dependencies)
  .settings(compilerSettings)
  .settings(
    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  )

lazy val compilerSettings = Seq(
  scalaVersion := "2.12.4",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-Ypartial-unification",
    "-language:experimental.macros",
    "-language:implicitConversions"
  )
)

lazy val dependencies = Seq(
  libraryDependencies ++= Seq(
    "com.github.pureconfig"          %% "pureconfig"           % "0.7.2",
    "com.softwaremill.quicklens"     %% "quicklens"            % "1.4.11",
    "com.typesafe.akka"              %% "akka-actor"           % "2.5.11",
    "com.typesafe.akka"              %% "akka-http"            % "10.1.1",
    "de.heikoseeberger"              %% "akka-http-circe"      % "1.20.1",
    "io.circe"                       %% "circe-core"           % "0.9.3",
    "io.circe"                       %% "circe-generic"        % "0.9.3",
    "io.circe"                       %% "circe-generic-extras" % "0.9.3",
    "io.circe"                       %% "circe-java8"          % "0.9.3",
    "io.circe"                       %% "circe-jawn"           % "0.9.3",
    "org.atnos"                      %% "eff"                  % "5.0.0",
    "org.atnos"                      %% "eff-monix"            % "5.0.0",
    "org.typelevel"                  %% "cats-core"            % "1.1.0",
    "co.fs2"                         %% "fs2-core"             % "0.10.3",
    "com.github.julien-truffaut"     %% "monocle-core"         % "1.5.1-cats",
    "com.github.julien-truffaut"     %% "monocle-macro"        % "1.5.1-cats",
    "org.http4s"                     %% "http4s-blaze-client"  % "0.18.7",
    "org.http4s"                     %% "http4s-circe"         % "0.18.7",
    "org.zalando"                    %% "grafter"              % "2.3.0",
    "ch.qos.logback"                 %  "logback-classic"      % "1.2.3",
    "com.typesafe.scala-logging"     %% "scala-logging"        % "3.7.2",
    "org.scalatest"                  %% "scalatest"            % "3.0.5"   % "it,test",
    "org.scalacheck"                 %% "scalacheck"           % "1.13.5"  % "it,test",
    "com.typesafe.akka"              %% "akka-http-testkit"    % "10.1.1"  % "it,test",
    compilerPlugin("org.spire-math"  %% "kind-projector"       % "0.9.6"),
    compilerPlugin("org.scalamacros" %% "paradise"             % "2.1.1" cross CrossVersion.full)
  )
)
