resolvers ++= Seq(
      "Sonatype Releases"  at "https://oss.sonatype.org/content/repositories/releases/",
      "Sonatype Backup" at "https://oss.sonatype.org/service/local/repositories/releases/content/",
      "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
      "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
      "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
      "Typesafe Backup Releases " at "http://repo.typesafe.com/typesafe/repo/")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.3.0-SNAPSHOT")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.6.2")
