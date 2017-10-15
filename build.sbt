import com.typesafe.sbt.SbtScalariform
import sbt.Keys.{resolvers, scalaVersion}


lazy val root = (project in file("."))
  .enablePlugins(ScalaUnidocPlugin)

  .settings(
    organization := "com.vicpara",
    name := "certa",
    releaseVersionFile := file("version.sbt"),
    scalaVersion := "2.12.2",
    crossScalaVersions := Seq("2.12.2"),
    scalacOptions ++= List("-deprecation", "-feature"),
    autoScalaLibrary := false,
    ivyScala := ivyScala.value.map(_.copy(overrideScalaVersion = true)),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    retrieveManaged := true,

    shellPrompt in ThisBuild := { state => scala.Console.MAGENTA + name.value + scala.Console.RESET + " > " },
    testOptions in Test += Tests.Argument("-oD", "-showtimes"),
    fork in(Test, run) := true,

    publishTo := Some(if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging),

    resolvers ++= Seq(
      "mvnrepository" at "https://repository.cloudera.com/artifactory/cloudera-repos/",
      "Maven Central" at "https://repo1.maven.org/maven2/",
      "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
    ),

    libraryDependencies ++= Seq(

      "org.apache.commons" % "commons-csv" % "1.2" withSources() withJavadoc(),
      "org.rogach" %% "scallop" % "2.1.1" withSources() withJavadoc(),
      "org.scalaz" %% "scalaz-core" % "7.2.10" withSources() withJavadoc(),

      "org.joda" % "joda-convert" % "1.8.1" withSources() withJavadoc(),
      "joda-time" % "joda-time" % "2.9.4" withSources() withJavadoc(),

      "log4j" % "log4j" % "1.2.17" withSources() withJavadoc(),

      "org.scalaj" %% "scalaj-http" % "2.3.0" withSources() withJavadoc(),
      "org.scalaz" % "scalaz-concurrent_2.12" % "7.2.11" withSources() withJavadoc(),

      "org.specs2" % "specs2-core_2.12" % "3.8.9" % "test" withSources() withJavadoc(),
      "org.specs2" %% "specs2-scalacheck" % "3.8.9" % "test" withSources() withJavadoc(),
      "com.fasterxml.jackson.module" % "jackson-module-scala_2.12" % "2.7.9" withSources() withJavadoc(),

      "org.apache.tika" % "tika-core" % "1.13" withSources() withJavadoc(),

      "org.apache.poi" % "poi-ooxml" % "3.16" withSources() withJavadoc(),
      "org.apache.poi" % "poi-scratchpad" % "3.16" withSources() withJavadoc(),
      "org.apache.poi" % "poi" % "3.16" withSources() withJavadoc(),

      "org.apache.pdfbox" % "pdfbox" % "2.0.7" withSources() withJavadoc(),
      "org.bouncycastle" % "bcprov-jdk15on" % "1.57" withSources() withJavadoc()
    ),

    assemblyMergeStrategy in assembly := {
      case el if el.contains("fasterxml.jackson.core") => MergeStrategy.first
      case el if el.contains("guava") => MergeStrategy.first

      case x if Assembly.isConfigFile(x) => MergeStrategy.concat
      case PathList(ps@_*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) => MergeStrategy.rename
      case PathList("META-INF", xs@_*) => (xs.map(_.toLowerCase) match {
        case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) => MergeStrategy.discard
        case ps@(x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") => MergeStrategy.discard
        case "plexus" :: xs => MergeStrategy.discard
        case "services" :: xs => MergeStrategy.filterDistinctLines
        case ("spring.schemas" :: Nil) | ("spring.handlers" :: Nil) => MergeStrategy.filterDistinctLines
        case _ => MergeStrategy.first // Changed deduplicate to first
      })
      case PathList(_*) => MergeStrategy.first
    }
  )


















pomIncludeRepository := { _ => false }

scalariformSettings
