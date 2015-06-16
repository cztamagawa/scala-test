import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._

object MyScalatraWebAppBuild extends Build {
  val Organization = "com.example"
  val Name = "My Scalatra Web App"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.6"
  val ScalatraVersion = "2.4.0.RC1"

  lazy val project = Project (
    "my-scalatra-web-app",
    file("."),
    settings = ScalatraPlugin.scalatraWithJRebel ++ scalateSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
      dependencyOverrides := Set(
        "org.scala-lang" %  "scala-library"  % scalaVersion.value,
        "org.scala-lang" %  "scala-reflect"  % scalaVersion.value,
        "org.scala-lang" %  "scala-compiler" % scalaVersion.value
      ),
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-specs2" % ScalatraVersion % "test",
        "org.scalatra" %% "scalatra-json" % "2.3.0",
        "org.json4s"   %% "json4s-jackson" % "3.2.9",
        "org.scalikejdbc" %% "scalikejdbc"       % "2.2.7",
        "org.scalikejdbc" %% "scalikejdbc-test" % "2.2.7" % "test",
        "org.skinny-framework" %% "skinny-assets"             % "1.3.18",
        "org.skinny-framework" %% "skinny-framework"          % "1.3.18",
        "org.skinny-framework" %% "skinny-oauth2-controller"  % "1.3.18",
        "org.skinny-framework" %% "skinny-twitter-controller" % "1.3.18",
        "org.skinny-framework" %% "skinny-scaldi"             % "1.3.18",
        "org.skinny-framework" %% "skinny-test"               % "1.3.18" % "test",
        "org.eclipse.jetty" % "jetty-webapp" % "9.1.5.v20140505" % "container",
        "org.eclipse.jetty" % "jetty-plus" % "9.1.5.v20140505" % "container",
        "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
        "javax.servlet" % "javax.servlet-api" % "3.1.0",
        "mysql" % "mysql-connector-java" % "5.1.29"
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      }
    )
  )
}
