name := "cinnamon-slf4j-mdc-test"

organization in ThisBuild := "com.github.dnvriend"

version in ThisBuild := "1.0.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.11.8"

fork in Test in ThisBuild := true
parallelExecution in Test in ThisBuild := false

lazy val root = (project in file("."))
  .aggregate(`futures-test`, `akka-test`)

lazy val `futures-test` = (project in file("futures-test"))
  .enablePlugins(AutomateHeaderPlugin)
  .enablePlugins(SbtScalariform)
  .enablePlugins(Cinnamon)
  .settings(commonSettings)
  .settings(libraryDependencies += Cinnamon.library.cinnamonSlf4jMdc)
  .settings(libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.17")
  .settings(libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.2")
  .settings(libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.4.17" % Test)
  .settings(libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % Test)
  // Add the Monitoring Agent for run and test
  .settings(cinnamon in run := true)
  .settings(cinnamon in test := true)
  // Set the Monitoring Agent log level
  .settings(cinnamonLogLevel := "INFO")

lazy val `akka-test` = (project in file("akka-test"))
  .enablePlugins(AutomateHeaderPlugin)
  .enablePlugins(SbtScalariform)
  .enablePlugins(Cinnamon)
  .settings(commonSettings)
  .settings(libraryDependencies += Cinnamon.library.cinnamonSlf4jMdc)
  .settings(libraryDependencies += Cinnamon.library.cinnamonCHMetrics)
  .settings(libraryDependencies += Cinnamon.library.cinnamonAkka)
  .settings(libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.17")
  .settings(libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.4.17")
  .settings(libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.2")
  .settings(libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.4.17" % Test)
  .settings(libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % Test)
  // you need to enable cinnamon like this
  .settings(cinnamon in run := true)
  .settings(cinnamon in test := true)
  // Set the Monitoring Agent log level
  .settings(cinnamonLogLevel := "INFO")

// enable scala code formatting //
import com.lightbend.cinnamon.sbt.Cinnamon.CinnamonKeys.cinnamon

import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import de.heikoseeberger.sbtheader.license.Apache2_0

lazy val commonSettings = Seq(
  licenses += ("Apache-2.0", url("http://opensource.org/licenses/apache2.0.php")),
  SbtScalariform.autoImport.scalariformPreferences := SbtScalariform.autoImport.scalariformPreferences.value
    .setPreference(AlignSingleLineCaseStatements, true)
    .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
    .setPreference(DoubleIndentClassDeclaration, true),
  headers := Map(
    "scala" -> Apache2_0("2016", "Dennis Vriend"),
    "conf" -> Apache2_0("2016", "Dennis Vriend", "#")
  )
)
