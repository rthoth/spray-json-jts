lazy val root = (project in file(".")).
   settings(
      inThisBuild(List(
         organization := "br.ufla",
         scalaVersion := "2.12.2",
         version      := "0.0.1"
      )),
      name := "spray-json-jts",
      crossScalaVersions := Seq("2.11.11", "2.12.2"),
      libraryDependencies ++= Seq(
         "com.vividsolutions" % "jts-core" % "1.14.0" % Provided,
         "io.spray" %%  "spray-json" % "1.3.3" % Provided,
         "org.scalatest" %% "scalatest" % "3.0.1" % Test
      ),
      bintrayOrganization := Some("lemaf"),
      licenses += ("LGPL-3.0", url("https://opensource.org/licenses/LGPL-3.0"))
   )
