lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "br.ufla",
      scalaVersion := "2.12.4",
      version      := "1.0.0"
    )),
    name := "spray-json-jts",
    crossScalaVersions := Seq("2.11.12", "2.12.4"),
    scalacOptions ++= Seq("-feature"),
    libraryDependencies ++= Seq(
      "com.vividsolutions" % "jts-core" % "1.14.0" % Provided,
      "io.spray" %%  "spray-json" % "1.3.3" % Provided,
      "org.scalatest" %% "scalatest" % "3.0.1" % Test
    ),
    licenses += ("LGPL-3.0", url("https://opensource.org/licenses/LGPL-3.0"))
  )
