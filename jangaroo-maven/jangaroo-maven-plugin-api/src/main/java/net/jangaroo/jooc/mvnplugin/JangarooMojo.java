package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.util.MavenPluginHelper;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.annotation.Nonnull;

public abstract class JangarooMojo extends AbstractMojo {

  private MavenPluginHelper mavenPluginHelper;

  /**
   * The maven project.
   */
  @Parameter(defaultValue = "${project}", required = true, readonly = true)
  private MavenProject project;

  @Nonnull // required property
  protected MavenProject getProject() {
    return project;
  }


  protected MavenPluginHelper getMavenPluginHelper() {
    if (mavenPluginHelper == null) {
      mavenPluginHelper = new MavenPluginHelper(getProject(), getLog());
    }
    return mavenPluginHelper;
  }

}