resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Bintray sbt plugin releases" at "http://dl.bintray.com/sbt/sbt-plugin-releases/"

resolvers += Resolver.url("heroku-sbt-plugin-heroku", url("http://dl.bintray.com/heroku/sbt-plugins"))(Resolver.ivyStylePatterns)

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.4")

// web plugins
addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.5")

addSbtPlugin("org.jruby" % "sbt-rubygems" % "1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "0.7.4" excludeAll(ExclusionRule("org.bouncycastle", "*")))

addSbtPlugin("com.heroku" % "sbt-heroku" % "0.1.1")