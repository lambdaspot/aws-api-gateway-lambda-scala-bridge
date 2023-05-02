enablePlugins(PackPlugin)

name         := "aws-lambda-scala-bridge"
version      := "0.1.4"
scalaVersion := "3.2.2"
organization := "dev.lambdaspot"
javacOptions ++= Seq("-source", "11", "-target", "11")

lazy val root = project
  .in(file("."))
  .settings(
    libraryDependencies ++= Seq(
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core"   % Version.Jsoniter,
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % Version.Jsoniter  % "provided",
      "com.amazonaws"                          % "aws-lambda-java-core"  % Version.AwsLambdaCore,
      "org.scalatest"                         %% "scalatest"             % Version.ScalaTest % Test
    )
  )
