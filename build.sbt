lazy val root = (project in file(".")).
   settings(
      inThisBuild(List(
         organization := "br.ufla",
         scalaVersion := "2.12.1",
         version      := "0.0.1"
      )),
      name := "spray-json-jts",
      libraryDependencies ++= Seq(
         "com.vividsolutions" % "jts-core" % "1.14.0" % Provided,
         "io.spray" %%  "spray-json" % "1.3.3" % Provided,
         "org.scalatest" %% "scalatest" % "3.0.1" % Test
      )
   )
