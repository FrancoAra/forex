name := "forex"
version := "1.0.0"

scalaVersion := "2.12.4"
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

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

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
  "org.zalando"                    %% "grafter"              % "2.3.0",
  "ch.qos.logback"                 %  "logback-classic"      % "1.2.3",
  "com.typesafe.scala-logging"     %% "scala-logging"        % "3.7.2",
  compilerPlugin("org.spire-math"  %% "kind-projector"       % "0.9.6"),
  compilerPlugin("org.scalamacros" %% "paradise"             % "2.1.1" cross CrossVersion.full)
)
