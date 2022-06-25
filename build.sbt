ThisBuild / version := "0.1.0-SNAPSHOT"

//ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "ukma-market"
  )

val scala2Version = "2.13.8"
val scala3Version = "3.1.2"
// To cross compile with Scala 2 and Scala 3
crossScalaVersions := Seq(scala2Version, scala3Version)
scalaVersion := scala2Version


// Append -deprecation to the options passed to the Scala compiler
scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xcheckinit", "-encoding", "utf8", "-Ymacro-annotations")

// Point to location of a snapshot repository for ScalaFX
resolvers += Opts.resolver.sonatypeSnapshots

//resolvers += Opts.resolver.sonatypeStaging

// Add ScalaFX dependency, exclude JavaFX transitive dependencies, may not mach this OS
//libraryDependencies += "org.scalafx" %% "scalafx" % "18.0.1-R27"
libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "18.0.1-R27",
  "org.scalafx" %% "scalafxml-core-sfx8" % "0.5"
)

// Add OS specific JavaFX dependencies
val javafxModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}
libraryDependencies ++= javafxModules.map(m => "org.openjfx" % s"javafx-$m" % "18.0.1" classifier osName)


libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "org.slf4j" % "slf4j-nop" % "1.7.36",
  "org.scalatest" % "scalatest_2.13" % "3.2.12" % "test",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3"
)

// Fork a new JVM for 'run' and 'test:run'
fork := true