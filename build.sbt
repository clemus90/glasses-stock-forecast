val scala3Version = "3.0.1"

val Http4sVersion = "1.0.0-M23"
val CirceVersion = "0.14.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "stock-forecast",
    version := "0.1.0",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "io.circe" %% "circe-generic" % CirceVersion
    )
  )
