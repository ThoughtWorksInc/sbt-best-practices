package com.thoughtworks.sbtBestPractice.packageunidoc

import sbt._
import Keys._
import sbt.Defaults.packageTaskSettings
import sbt.plugins.JvmPlugin
import sbtunidoc.{BaseUnidocPlugin, JavaUnidocPlugin, ScalaUnidocPlugin, UnidocKeys}

/**
  * @author 杨博 (Yang Bo) &lt;pop.atry@gmail.com&gt;
  */
object PackageUnidoc extends AutoPlugin with UnidocKeys {

  override def requires: Plugins = JvmPlugin

  override def trigger: PluginTrigger = allRequirements

  object autoImport {
    val unidocProject =
      Project("unidoc", file("unidoc"))
        .enablePlugins(ScalaUnidocPlugin, JavaUnidocPlugin)
        .settings(
          publishArtifact := false,
          unidocProjectFilter in ScalaUnidocPlugin.autoImport.ScalaUnidoc in BaseUnidocPlugin.autoImport.unidoc := inAnyProject
        )
  }
  import autoImport._

  override val extraProjects: Seq[Project] = Seq(unidocProject)

  override def projectSettings: Seq[Def.Setting[_]] = {
    inConfig(Compile)(
      packageTaskSettings(
        packageDoc,
        (unidoc in Compile in unidocProject).map(_.flatMap(Path.allSubpaths))
      )
    )
  }

}
