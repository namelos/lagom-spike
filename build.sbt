organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.2.5" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1" % Test
val postgres = "org.postgresql" % "postgresql" % "9.4.1212"

lazy val `hello` = (project in file("."))
  .aggregate(`counter-api`, `counter-impl`, `hello-api`, `hello-impl`, `hello-stream-api`, `hello-stream-impl`, `front-end`)

lazy val `counter-api` = (project in file("counter-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `counter-impl` = (project in file("counter-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
//      lagomScaladslPersistenceCassandra,
      postgres,
      lagomScaladslPersistenceJdbc,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`counter-api`)

lazy val `hello-api` = (project in file("hello-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `hello-impl` = (project in file("hello-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
//      lagomScaladslPersistenceCassandra,
      postgres,
      lagomScaladslPersistenceJdbc,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`hello-api`)

lazy val `hello-stream-api` = (project in file("hello-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `hello-stream-impl` = (project in file("hello-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`hello-stream-api`, `hello-api`)

lazy val `front-end` = (project in file("front-end"))
  .enablePlugins(PlayScala && LagomPlay)
  .dependsOn(`counter-api`)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslServer,
      macwire,
      scalaTest
    )
  )