package net.jangaroo.jooc.mvnplugin;

import net.jangaroo.jooc.mvnplugin.util.MavenPluginHelper;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

public abstract class JangarooMojo extends AbstractMojo {

  private MavenPluginHelper mavenPluginHelper;

  protected abstract MavenProject getProject();

  protected MavenPluginHelper getMavenPluginHelper() {
    if (mavenPluginHelper == null) {
      if (getProject() == null || getLog() == null) {
        throw new IllegalStateException("getMavenPluginHelper() called too early!");
      }
      mavenPluginHelper = new MavenPluginHelper(getProject(), getLog());
    }
    return mavenPluginHelper;
  }

}