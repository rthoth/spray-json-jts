lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "br.ufla",
      scalaVersion := "2.12.4",
      version      := "1.0.0",
      description := "This is a small library for writing and reading GeoJSON with spray-json."
    )),
    name := "spray-json-jts",
    crossScalaVersions := Seq("2.11.12", "2.12.4"),
    scalacOptions ++= Seq("-feature"),
    libraryDependencies ++= Seq(
      "com.vividsolutions" % "jts-core" % "1.14.0" % Provided,
      "io.spray" %%  "spray-json" % "1.3.3" % Provided,
      "org.scalatest" %% "scalatest" % "3.0.1" % Test
    ),
    licenses += ("LGPL-3.0", url("https://opensource.org/licenses/LGPL-3.0")),
    homepage := Some(url("https://github.com/rthoth/spray-json-jts")),
    pomIncludeRepository := { _ => false },
    publishMavenStyle := true,
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/rthoth/spray-json-jts"),
        "scm:git@github.com:rthoth/spray-json-jts.git"
      )
    ),
    developers := List(
      Developer(
        id = "rthoth",
        name = "Ronaldo",
        email = "ronaldo.asilva@gmail.com",
        url = url("https://github.com/rthoth")
      )
    ),
    publishTo := Some("Bintray" at "https://api.bintray.com/maven/rthoth/releases/spray-json-jts/;publish=1"),
    credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")
  )
