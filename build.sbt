name := "root"

organization  := "com.example"

version       := "0.1"

scalaVersion  := "2.10.0"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  "spray repo" at "http://repo.spray.io/",
  "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies ++= Seq(
  "io.spray"            %   "spray-can"     % "1.1-M7",
  "io.spray"            %   "spray-routing" % "1.1-M7",
  "io.spray"            %%  "spray-json"    % "1.2.3",
  "io.spray"            %   "spray-testkit" % "1.1-M7",
  "com.typesafe.akka"   %%  "akka-actor"    % "2.1.0",
  "org.specs2"          %%  "specs2"        % "1.13" % "test",
  "net.databinder" %% "unfiltered-filter" % "0.6.7"
)

seq(Revolver.settings: _*)
