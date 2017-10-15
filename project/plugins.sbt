scalacOptions += "-deprecation"

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.4")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.5")

addSbtPlugin("com.typesafe.sbt" % "sbt-osgi" % "0.7.0")

addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")

addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.4.0")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.5")

addSbtPlugin("com.github.tkawachi" % "sbt-doctest" % "0.3.2")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "1.1")

fullResolvers ~= {_.filterNot(_.name == "jcenter")}
