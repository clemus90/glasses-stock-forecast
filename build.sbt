val scala3Version = "3.0.1"

val ZIOHttpVersion = "1.0.0.0-RC17"

lazy val root = project
  .in(file("."))
  .settings(
    name := "stock-forecast",
    version := "0.1.0",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "io.d11" %% "zhttp" % ZIOHttpVersion
    )
  )
