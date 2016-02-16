package net.jangaroo.jooc.mvnplugin.sencha;

import net.jangaroo.jooc.mvnplugin.SenchaConfiguration;
import net.jangaroo.jooc.mvnplugin.sencha.configurer.Configurer;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * A set of helper functions for handling sencha package structure.
 */
abstract class AbstractSenchaHelper implements SenchaHelper {

  private final MavenProject project;
  private final SenchaConfiguration senchaConfiguration;
  private final Log log;

  private final String senchaModuleName;

  public AbstractSenchaHelper(MavenProject project, SenchaConfiguration senchaConfiguration, Log log) {
    this.project = project;
    this.senchaConfiguration = senchaConfiguration;
    this.log = log;

    this.senchaModuleName = SenchaUtils.getSenchaPackageNameForMavenProject(project);
  }

  protected void copyFilesFromJoo(String path) throws MojoExecutionException {
    File jangarooResourcesDir = new File(project.getBuild().getDirectory() + "/classes/META-INF/resources");
    File senchaResourcesDir = new File(path + File.separator + SenchaUtils.SENCHA_RELATIVE_RESOURCES_PATH);
    if (jangarooResourcesDir.exists()) {
      if (senchaResourcesDir.exists()) {
        try {
          FileUtils.deleteDirectory(senchaResourcesDir);
        } catch (IOException e) {
          throw new MojoExecutionException("could not clean resources folder in sencha package", e);
        }
      }
      try {
        FileUtils.copyDirectory(jangarooResourcesDir, senchaResourcesDir);
      } catch (IOException e) {
        throw new MojoExecutionException("could not copy classes", e);
      }
    }

    File jangarooClassDir = new File(senchaResourcesDir.getAbsolutePath() + File.separator + "joo/classes");
    if (jangarooClassDir.exists()) {
      File senchaClassDir = new File(path + File.separator + SenchaUtils.SENCHA_RELATIVE_CLASS_PATH);
      if (senchaClassDir.exists()) {
        try {
          FileUtils.deleteDirectory(senchaClassDir);
        } catch (IOException e) {
          throw new MojoExecutionException("could not clean class folder in sencha package", e);
        }
      }
      try {
        FileUtils.moveDirectory(jangarooClassDir, senchaClassDir);
      } catch (IOException e) {
        throw new MojoExecutionException("could not copy classes", e);
      }
    }

    File jangarooOverridesDir = new File(senchaResourcesDir.getAbsolutePath() + File.separator + "joo/overrides");
    if (jangarooOverridesDir.exists()) {
      File senchaOverridesDir = new File(path + File.separator + SenchaUtils.SENCHA_RELATIVE_OVERRIDES_PATH);
      if (senchaOverridesDir.exists()) {
        try {
          FileUtils.deleteDirectory(senchaOverridesDir);
        } catch (IOException e) {
          throw new MojoExecutionException("could not clean overrides folder in sencha package", e);
        }
      }
      try {
        FileUtils.moveDirectory(jangarooOverridesDir, senchaOverridesDir);
      } catch (IOException e) {
        throw new MojoExecutionException("could not copy overrides", e);
      }
    }
  }

  protected MavenProject getProject() {
    return project;
  }

  protected SenchaConfiguration getSenchaConfiguration() {
    return senchaConfiguration;
  }

  protected Log getLog() {
    return log;
  }

  protected String getSenchaModuleName() {
    return senchaModuleName;
  }

  protected Map<String, Object> getConfig(Configurer[] configurers) throws MojoExecutionException {
    Map<String, Object> config = new LinkedHashMap<String, Object>();

    for (Configurer configurer : configurers) {
      configurer.configure(config);
    }

    return config;
  }

}
